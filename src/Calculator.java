import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.io.Serializable;

public class Calculator implements Serializable {

    private String expression;

    private String result;

    public Calculator(String expression) {
        this.expression = expression;
        this.result = "0";
    }

    public String getResult() {
        return result;
    }

    public void calculate() {

        //converting string into expression
        try {
            ScriptEngineManager mgr = new ScriptEngineManager();
            ScriptEngine engine = mgr.getEngineByName("JavaScript");
            result =  engine.eval(expression).toString();
        } catch (ScriptException e) {
            e.printStackTrace();
            result = "Syntax error";
        }
    }

}
