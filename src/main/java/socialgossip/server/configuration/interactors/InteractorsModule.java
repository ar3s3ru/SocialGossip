package socialgossip.server.configuration.interactors;

import dagger.Binds;
import dagger.Module;
import socialgossip.server.usecases.login.LoginErrors;
import socialgossip.server.usecases.login.LoginInteractor;
import socialgossip.server.usecases.login.LoginOutput;
import socialgossip.server.usecases.login.LoginUseCase;
import socialgossip.server.usecases.logout.LogoutErrors;
import socialgossip.server.usecases.logout.LogoutInteractor;
import socialgossip.server.usecases.logout.LogoutUseCase;
import socialgossip.server.usecases.registration.RegistrationErrors;
import socialgossip.server.usecases.registration.RegistrationInteractor;
import socialgossip.server.usecases.registration.RegistrationUseCase;

@Module
public abstract class InteractorsModule {
    @Binds
    @UseCaseScope
    abstract RegistrationUseCase<Void, RegistrationErrors> provideRegistrationUseCaseFrom(
            RegistrationInteractor interactor
    );

    @Binds
    @UseCaseScope
    abstract LoginUseCase<LoginOutput, LoginErrors> provideLoginUseCaseFrom(
            LoginInteractor interactor
    );

    @Binds
    @UseCaseScope
    abstract LogoutUseCase<Boolean, LogoutErrors> provideLogoutUseCaseFrom(
            LogoutInteractor interactor
    );
}