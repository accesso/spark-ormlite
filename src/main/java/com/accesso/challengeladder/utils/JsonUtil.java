package com.accesso.challengeladder.utils;

import spark.ResponseTransformer;

import com.google.gson.Gson;

public class JsonUtil
{

    public static String toJson(Object object)
    {
        return new Gson().toJson(object);
    }

    public static ResponseTransformer json()
    {
        return JsonUtil::toJson;
    }
}