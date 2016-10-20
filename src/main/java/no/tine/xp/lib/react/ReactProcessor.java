package no.tine.xp.lib.react;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;

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
        	
	        nashorn.eval(getFile("react.min.js"));
	        nashorn.eval(getFile("react-dom-server.min.js"));
		        
	        html += (String)nashorn.eval("ReactDOMServer.renderToString(React.createElement('div', null, 'Hello World From Java!'));");
        }
        catch(IOException e) {
        	html += "Unable to find included file: " + e.getMessage();
        }
        
        return html;
    }
    
    private String getFile(String filename) throws IOException {
    	InputStream is = ReactProcessor.class.getClassLoader().getResourceAsStream(filename);
    	List<String> lines;
    	try {
    		lines = IOUtils.readLines(is, "UTF-8");
    	}
    	finally {
    		is.close();
    	}
    	return StringUtils.join(lines, "\n");
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