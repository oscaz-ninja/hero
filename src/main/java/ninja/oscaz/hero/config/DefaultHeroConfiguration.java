package ninja.oscaz.hero.config;

import java.util.logging.Logger;
import ninja.oscaz.hero.Hero;
import ninja.oscaz.hero.bind.binder.Singleton;
import ninja.oscaz.hero.bind.binder.SingletonBinder;
import ninja.oscaz.hero.transform.initialize.InitializeTransformer;
import ninja.oscaz.hero.transform.intercept.InterceptionTransformer;
import ninja.oscaz.hero.transform.intercept.aspects.Async;
import ninja.oscaz.hero.transform.intercept.aspects.AsyncInterceptor;
import ninja.oscaz.hero.transform.intercept.aspects.Audit;
import ninja.oscaz.hero.transform.intercept.aspects.AuditInterceptor;

public class DefaultHeroConfiguration implements HeroConfiguration {

    @Override
    public void defaultBindings(Hero hero) {
        hero.bind(Singleton.class).to(SingletonBinder.class);
        hero.bind(Async.class).to(AsyncInterceptor.class);
        hero.bind(Audit.class).to(AuditInterceptor.class);
        hero.bind(Logger.class).toValue(Logger.getGlobal());
    }

    @Override
    public void defaultTransformers(Hero hero) {
        hero.addTransformer(InitializeTransformer.class);
        hero.addTransformer(InterceptionTransformer.class);
    }

}
