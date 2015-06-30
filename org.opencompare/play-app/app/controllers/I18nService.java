package controllers;

import jsmessages.JsMessages;
import jsmessages.JsMessagesFactory;
import play.api.libs.json.JsObject;
import play.api.libs.json.JsValue;
import play.mvc.Controller;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static scala.collection.JavaConversions.mapAsJavaMap;
import static scala.collection.JavaConversions.setAsJavaSet;

/**
 * Created by gbecan on 6/26/15.
 */
@Singleton
public class I18nService {


    private final JsMessages jsMessages;

    @Inject
    public I18nService(JsMessagesFactory jsMessagesFactory) {
        jsMessages = jsMessagesFactory.all();

    }

    public Map<String, String> getMessages(String language) {
        return mapAsJavaMap(jsMessages.allMessages().apply(language));
    }

    public JsValue getMessagesJson(String language) {
        JsObject allMessages = (JsObject) jsMessages.allMessagesJson();
        JsValue messages = allMessages.value().apply(language);
        return messages;
    }

    public boolean isDefined(String language) {
        return jsMessages.allMessages().contains(language);
    }




}
