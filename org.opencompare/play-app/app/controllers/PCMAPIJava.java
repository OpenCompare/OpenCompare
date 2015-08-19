package controllers;

import play.mvc.Controller;
import play.mvc.Result;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by gbecan on 8/18/15.
 */
@Singleton
public class PCMAPIJava extends Controller {


    @Inject
    private I18nService i18nService;


    public Result i18n() {
        return ok(i18nService.getMessagesJson(lang().code()).toString());
    }

    public Result setLang(String language) {
        if (i18nService.isDefined(language)) {
            changeLang(language.toUpperCase());
            return ok("");
        } else {
            clearLang();
            return ok("language unknown");
        }
    }



}
