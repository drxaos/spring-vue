package com.example.demo.service.auth;

import com.oracle.truffle.js.scriptengine.GraalJSScriptEngine;
import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.HostAccess;
import org.springframework.stereotype.Service;

import javax.script.ScriptEngine;
import javax.script.ScriptException;

@Service
public class FrontendService {

    public void exec() throws ScriptException {
        ScriptEngine engine = GraalJSScriptEngine.create(null,
                Context.newBuilder("js")
                        .allowHostAccess(HostAccess.ALL)
                        .allowHostClassLookup(s -> true)
                        .option("js.ecmascript-version", "2021"));
        engine.put("javaObj", new Object());
        Object result = engine.eval("(javaObj instanceof Java.type('java.lang.Object'));");
        System.out.println(result);
    }

}
