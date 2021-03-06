package com.qiniu.qbox.rs;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import com.qiniu.qbox.auth.CallRet;

public class BatchStatRet extends CallRet {

	public List<StatRet> results = new ArrayList<StatRet>();

	public BatchStatRet(CallRet ret) {

		super(ret);
		if (ret.ok() && ret.getResponse() != null) {
			try {
				unmarshal(ret.getResponse());
			} catch (Exception e) {
				e.printStackTrace();
				this.exception = e;
			}
		}
	}

	public void unmarshal(String response) throws JSONException {
		
		JSONTokener tokens = new JSONTokener(response);
		JSONArray arr = new JSONArray(tokens);
		
		for (int i = 0; i < arr.length(); i++) {
			JSONObject jsonObj = arr.getJSONObject(i);
			if (jsonObj.has("code") && jsonObj.has("data")) {
				int code = jsonObj.getInt("code");
				JSONObject body = jsonObj.getJSONObject("data");
				CallRet ret = new CallRet(code, body.toString());
				StatRet statRet = new StatRet(ret);
				results.add(statRet);
			} else {
				new JSONException("Bad BatchStat result!");
			}
		}
	}
	
}
