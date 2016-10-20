package no.tine.xp.lib.react;

import com.enonic.xp.resource.ResourceService;
import com.enonic.xp.script.bean.BeanContext;
import com.enonic.xp.script.bean.ScriptBean;

public final class ReactService implements ScriptBean
{
    private ResourceService resourceService;

    public ReactService()
    {
    }

    public Object newProcessor()
    {
        ReactProcessor reactProcessor = new ReactProcessor();
        reactProcessor.setResourceService( resourceService );
        return reactProcessor;
    }

    @Override
    public void initialize( final BeanContext context )
    {
        this.resourceService = context.getService( ResourceService.class ).get();
    }
}