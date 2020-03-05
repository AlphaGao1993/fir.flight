package io.github.ryanhoo.firFlight.data.source.remote;

import io.github.ryanhoo.firFlight.data.model.Token;
import io.github.ryanhoo.firFlight.data.source.TokenContract;
import io.github.ryanhoo.firFlight.data.source.remote.api.RESTFulApiService;
import rx.Observable;

/**
 * Created with Android Studio.
 * User: ryan.hoo.j@gmail.com
 * Date: 7/3/16
 * Time: 9:52 PM
 * Desc: RemoteTokenDataSource
 */
public class RemoteTokenDataSource extends AbstractRemoteDataSource implements TokenContract.Remote {

    public RemoteTokenDataSource(RESTFulApiService api) {
        super(api);
    }

    @Override
    public Observable<Token> accessToken(String email, String password) {
        Token token = new Token();
        token.setApiToken("f2c61ed605266a5e720e05171fa30c1d");
        token.setAccessToken("f2c61ed605266a5e720e05171fa30c1d");
        return Observable.just(token);
//        return mApi.accessToken(email, password);
    }

    @Override
    public Observable<Token> apiToken() {
        Token token = new Token();
        token.setApiToken("f2c61ed605266a5e720e05171fa30c1d");
        token.setAccessToken("f2c61ed605266a5e720e05171fa30c1d");
        return Observable.just(token);
//        return mApi.apiToken();
    }

    @Override
    public Observable<Token> refreshApiToken() {
        return mApi.refreshApiToken();
    }
}
