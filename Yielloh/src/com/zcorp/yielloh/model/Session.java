package com.zcorp.yielloh.model;

import java.util.Set;

import android.content.Context;
import android.content.SharedPreferences;

public class Session{
	private Context context;
	private String sessionName;
	private SharedPreferences prefs;
	private SharedPreferences.Editor editor;
	/*Session(Context context,String sessionName){
		this.context=context;
		this.sessionName=sessionName;
		prefs=context.getSharedPreferences(sessionName,Context.MODE_PRIVATE);
	}*/
	
	protected Session createSession(Context context,String sessionName){
		Session s=new Session();
		s.context=context;
		s.sessionName=sessionName;
		s.prefs=context.getSharedPreferences(sessionName,Context.MODE_PRIVATE);
		return s;
	}
	
	protected void putString(String key,String value){
		editor=prefs.edit();
		editor.putString(key,value);
		editor.commit();
	}
	
	protected void putBoolean(String key,boolean value){
		editor=prefs.edit();
		editor.putBoolean(key, value);
		editor.commit();
	}
	
	protected void putInt(String key,int value){
		editor=prefs.edit();
		editor.putInt(key, value);
		editor.commit();
	}
	
	protected void putFloat(String key,float value){
		editor=prefs.edit();
		editor.putFloat(key, value);
		editor.commit();
	}
	
	protected void putLong(String key,long value){
		editor=prefs.edit();
		editor.putLong(key, value);
		editor.commit();
	}
	
	protected void putStringSet(String key,Set<String> value){
		editor=prefs.edit();
		editor.putStringSet(key, value);
		editor.commit();
	}
	
	protected boolean getBoolean(String key,boolean defaultValue){
		return prefs.getBoolean(key, defaultValue);
	}
	
	protected String getString(String key,String defaultValue){
		return prefs.getString(key, defaultValue);
	}
	
	protected float getFloat(String key,float defaultValue){
		return prefs.getFloat(key, defaultValue);
	}
	
	protected int getInt(String key,int defaultValue){
		return prefs.getInt(key, defaultValue);
	}
	
	protected long getLong(String key,long defValue){
		return prefs.getLong(key, defValue);
	}
	
	protected Set<String> getStringSet(String key,Set<String> defValue){
		return prefs.getStringSet(key, defValue);
	}
	
	protected void remove(String key){
		editor=prefs.edit();
		editor.remove(key);
		editor.commit();
	}
}
