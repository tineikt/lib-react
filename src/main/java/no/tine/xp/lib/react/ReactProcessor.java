package no.tine.xp.lib.react;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import com.enonic.xp.resource.ResourceKey;
import com.enonic.xp.resource.ResourceService;
import com.enonic.xp.script.ScriptValue;

public final class ReactProcessor
{
    private ResourceKey view;
    private ResourceService resourceService;
    private ScriptValue model;

    public ReactProcessor()
    { }

    public void setView( final ResourceKey view )
    {
        this.view = view;
    }

    public void setModel( final ScriptValue model )
    {
        this.model = model;
    }

    public String process()
    {
        try
        {
            return doProcess();
        }
        catch ( final ScriptException e ) {
        	throw handleError( e );
        }
        catch ( final RuntimeException e )
        {
            throw handleError( e );
        }
    }

    public void setResourceService( final ResourceService resourceService )
    {
        this.resourceService = resourceService;
    }

    private String doProcess() throws ScriptException
    {
        //final Resource resource = resourceService.getResource( this.view );
        //final Map<String, Object> map = this.model != null ? this.model.getMap() : Maps.newHashMap();

        ScriptEngine nashorn = new ScriptEngineManager().getEngineByName("nashorn");

        String html = "";
        try {
        	html += "Currently running in " + new File(".").getAbsolutePath();
	        nashorn.eval(new FileReader("/react.min.js"));
	        nashorn.eval(new FileReader("/react-dom-server.min.js"));
		        
	        html += (String)nashorn.eval("ReactDOMServer.renderToString(React.createElement('div', null, 'Hello World From Java!'));");
        }
        catch(FileNotFoundException e) {
        	html += "Unable to find included file: " + e.getMessage();
        }
        
        return html;
    }

    private RuntimeException handleError( final ScriptException e )
    {
        return new RuntimeException("Script error", e);
    }
    
    private RuntimeException handleError( final RuntimeException e )
    {
        return e;
    }
}