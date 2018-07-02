package socialgossip.server.configuration.interactors;

import dagger.Module;
import dagger.Provides;
import socialgossip.server.core.entities.password.PasswordValidator;
import socialgossip.server.core.gateways.notifications.Notifier;
import socialgossip.server.core.gateways.session.SessionInserter;
import socialgossip.server.core.gateways.user.UserFinder;
import socialgossip.server.usecases.login.LoginInteractor;
import socialgossip.server.usecases.login.SessionFactory;

@Module(includes = InteractorsModule.class)
public class LoginModule {
    @Provides
    @UseCaseScope static LoginInteractor providesLoginInteractor(
            final UserFinder        userFinder,
            final SessionInserter   sessionInserter,
            final SessionFactory    sessionFactory,
            final PasswordValidator passwordValidator,
            final Notifier          notifier
    ) {
        return new LoginInteractor(
                userFinder,
                sessionInserter,
                passwordValidator,
                sessionFactory,
                notifier
        );
    }
}
