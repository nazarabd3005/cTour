package com.giffar.ctour.callbacks;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

public interface OnParseJson<T> {

	public T parseJsonObject(JSONObject jObj);
	public List<T> parseJsonArray(JSONArray jArr);
	
}
