package edu.gac.mcs270.utilities;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.google.gdata.client.spreadsheet.SpreadsheetService;
import com.google.gdata.data.spreadsheet.Cell;
import com.google.gdata.data.spreadsheet.CellEntry;
import com.google.gdata.data.spreadsheet.CellFeed;
import com.google.gdata.data.spreadsheet.WorksheetEntry;
import com.google.gdata.data.spreadsheet.WorksheetFeed;

public class SpreadsheetDrivenInstantiator {
	
	private Map<Class<?>, Map<String, Object>> instances = new HashMap<Class<?>, Map<String, Object>>();

	public SpreadsheetDrivenInstantiator(String spreadsheetID,
			String packageName) {
		super();
		SpreadsheetService service = new SpreadsheetService("max.mcs.gac.edu-SpreadsheetDrivenInstantiator-0.0");
		WorksheetFeed feed;
		try {
			URL feedURL = new URL("https://spreadsheets.google.com/feeds/worksheets/" +
									spreadsheetID + "/public/values");
			feed = service.getFeed(feedURL, WorksheetFeed.class);
		} catch (Exception e) {
			throw new Error("Trouble getting worksheet feed", e);
		}
		for (WorksheetEntry worksheet : feed.getEntries()) {
			String title = worksheet.getTitle().getPlainText();
			String[] names = title.split("\\.");
			if(names.length < 1 || names.length > 2){
				throw new Error("Invalid worksheet name " + title);
			}
			Class<?> type;
			try {
				type = Class.forName(packageName + "." + names[0]);
			} catch (ClassNotFoundException e1) {
				throw new Error("Spreadsheet contained unknown class name", e1);
			}
			CellFeed cellFeed;
			try {
				cellFeed = service.getFeed(worksheet.getCellFeedUrl(), CellFeed.class);
			} catch (Exception e) {
				throw new Error("Trouble getting cell feed", e);
			}
			if(names.length == 2){
				invokeMethod(type, names[1], cellFeed);
			} else {
				instantiate(type, cellFeed);
			}
		}
	}
	
	private interface Converter {
		public Object convert(String s);
	}

	// TODO refactor commonality from invokeMethod, instantiate
	private <T> void invokeMethod(Class<?> type, String methodName, CellFeed feed) {
		Method method = null;
		int arity = 0, prevRow = 1, prevCol = 0;
		Object[] args = null;
		Converter[] converters = null;
		String key = null;
		ArrayList<String> columnHeadings = new ArrayList<String>();
		for(CellEntry entry : feed.getEntries()){
			Cell cell = entry.getCell();
			if(cell.getRow() == 1){
				int n = cell.getCol();
				arity = n - 1;
				if(n > 1){
					while(columnHeadings.size() < n-2)
						columnHeadings.add("");
					columnHeadings.add(cell.getValue());
				}
			}else{
				if(method == null){
					ArrayList<Method> candidateMethods = new ArrayList<Method>();
					for(Method m : type.getMethods()){
						if(m.getName().equals(methodName)){
							int mArity = m.getParameterTypes().length;
							if(mArity <= arity && m.isVarArgs())
								throw new Error("varargs constructors not yet supported");
							if(mArity == arity)
								candidateMethods.add(m);
						}
					}
					if(candidateMethods.size() == 0)
						throw new Error("No method of arity " + arity + " for " + type.getName() + "." + methodName);
					else if(candidateMethods.size() > 1){
						ArrayList<Method> survivors = new ArrayList<Method>();
						for(Method m : candidateMethods){
							survivors.add(m); // tentatively
							Class<?>[] types = m.getParameterTypes();
							for(int i = 0; i < types.length; i++){
								String name = types[i].getName();
								name = name.substring(name.lastIndexOf('.')+1);
								if(!name.equals(columnHeadings.get(i))){
									survivors.remove(survivors.size()-1);
									break;
								}
							}
						}
						candidateMethods = survivors;
						if(candidateMethods.size() == 0)
							throw new Error("Overloaded " + type.getName() + "." + methodName +
									" methods of arity " + arity + " but none matches column headings");
						else if(candidateMethods.size() > 1)
							throw new Error("Overloaded " + type.getName() + "." + methodName +
									" methods of arity " + arity + " not disambiguated by column headings");
					}
					method = candidateMethods.get(0);
					args = new Object[arity];
					converters = new Converter[arity];
					for(int i = 0; i < arity; i++){
						converters[i] = converterForType(method.getParameterTypes()[i]);
					}
				}
				if(prevRow != cell.getRow()){
					if(prevRow != 1){
						invokeFromRow(key, type, method, arity, prevCol, args, converters);
					}
					prevRow = cell.getRow();
					prevCol = 0;
					key = cell.getCol()==1 ? cell.getValue() : ""; // TODO consider more rigid checking
				}
				interpolateBlankColumns(cell.getCol()-1, prevCol, args, converters);
				prevCol = cell.getCol();
				if(prevCol != 1)
					args[prevCol-2] = converters[prevCol-2].convert(cell.getValue());
			}
		}
		if(method != null){
			invokeFromRow(key, type, method, arity, prevCol, args, converters);
		}
	}

	private void invokeFromRow(String key, Class<?> type, Method method,
			int arity, int prevCol, Object[] args, Converter[] converters) {
		interpolateBlankColumns(arity, prevCol, args, converters);
		Object o = converterForType(type).convert(key);
		try{
			method.invoke(o, args);
		} catch(Exception e){
			throw new Error("Trouble invoking " + type.getName() + "." + method.getName() + " on " + key,
					e);
		}
	}

	@SuppressWarnings("unchecked") // TODO is there a better option?
	private <T> void instantiate(Class<T> type, CellFeed feed) {
		ArrayList<Map<String, Object>> instanceMaps = new ArrayList<Map<String, Object>>();
		for(Class<?> c = type; c != null; c = c.getSuperclass()){
			Map<String, Object> m = instances.get(c);
			if(m == null){
				m = new HashMap<String, Object>();
				instances.put(c, m);
			}
			instanceMaps.add(m);
		}
		for(Class<?> c : type.getInterfaces()){
			Map<String, Object> m = instances.get(c);
			if(m == null){
				m = new HashMap<String, Object>();
				instances.put(c, m);
			}
			instanceMaps.add(m);
		}
		Constructor<T> constructor = null;
		int arity = 0, prevRow = 1, prevCol = 0;
		Object[] initArgs = null;
		Converter[] converters = null;
		String key = null;
		for(CellEntry entry : feed.getEntries()){
			Cell cell = entry.getCell();
			if(cell.getRow() == 1)
				arity = cell.getCol();
			else{
				if(constructor == null){
					ArrayList<Constructor<T>> candidateConstructors = new ArrayList<Constructor<T>>();
					for(Constructor<?> c : type.getConstructors()){
						int cArity = c.getParameterTypes().length;
						if(cArity <= arity && c.isVarArgs())
							throw new Error("varargs constructors not yet supported");
						if(cArity == arity)
							candidateConstructors.add((Constructor<T>) c);
					}
					if(candidateConstructors.size() == 0)
						throw new Error("No constructor of arity " + arity + " for " + type.getName());
					else if(candidateConstructors.size() > 1)
						throw new Error("Multiple constructors of arity " + arity + " for " + type.getName());
					constructor = candidateConstructors.get(0);
					initArgs = new Object[arity];
					converters = new Converter[arity];
					for(int i = 0; i < arity; i++){
						converters[i] = converterForType(constructor.getParameterTypes()[i]);
					}
				}
				if(prevRow != cell.getRow()){
					if(prevRow != 1){
//						System.err.println("Instantiating " + type.getName() + " " + key);
						instantiateRow(instanceMaps, key, constructor, arity, prevCol, initArgs, converters);
					}
					prevRow = cell.getRow();
					prevCol = 0;
					key = cell.getCol()==1 ? cell.getValue() : ""; // TODO consider more rigid checking
				}
				interpolateBlankColumns(cell.getCol()-1, prevCol, initArgs, converters);
				prevCol = cell.getCol();
//				System.err.println("Converting " + cell.getValue() + "(" + prevCol + "/" + arity + ")");
				initArgs[prevCol-1] = converters[prevCol-1].convert(cell.getValue());
			}
		}
		if(constructor != null){
//			System.err.println("Instantiating " + type.getName() + " " + key);
			instantiateRow(instanceMaps, key, constructor, arity, prevCol, initArgs, converters);
		}
	}

	private Converter converterForType(Class<?> type) {
		if(type == Integer.class || type == int.class){
			return new Converter(){
				public Object convert(String s){
					return Integer.parseInt(s);
				}
			};
		} else if(type == String.class){
			return new Converter(){
				public Object convert(String s){
					return s;
				}
			};
		} else if(instances.containsKey(type)){
			final Map<String, ?> map = instances.get(type);
			final String typeName = type.getName();
			return new Converter(){
				public Object convert(String s){
					Object o = map.get(s);
					if(o == null){
						throw new Error("No object named " + s + " was created of type " + typeName);
					}
					return o;
				}
			};
		} else {
			throw new Error("No string conversion yet implemented for type " + type.getName());
		}
	}

	private <T> void instantiateRow(ArrayList<Map<String, Object>> instanceMaps, String key,
			Constructor<T> constructor, int arity,
			int prevCol, Object[] initArgs, Converter[] converters)
			throws Error {
		interpolateBlankColumns(arity, prevCol, initArgs, converters);
		try {
			Object instance = constructor.newInstance(initArgs);
			for(Map<String,Object> m : instanceMaps)
				m.put(key, instance);
		} catch (Exception e) {
			throw new Error("Trouble invoking constructor", e);
		}
	}

	private void interpolateBlankColumns(int lastColumn, int prevCol, Object[] initArgs, Converter[] converters) {
		for(; prevCol < lastColumn; prevCol++){
			initArgs[prevCol] = converters[prevCol].convert("");
		}
	}

	public <T> T getInstance(Class<T> type, String key){
		return type.cast(getInstances(type).get(key));
	}
	
	@SuppressWarnings("unchecked") // TODO is there a better option?
	private <T> Map<String, T> getInstances(Class<T> type){
		Map<String, Object> instances = this.instances.get(type);
		if(instances == null){
			instances = new HashMap<String, Object>();
			this.instances.put(type, instances);
		}
		return (Map<String, T>) instances;
	}

}
