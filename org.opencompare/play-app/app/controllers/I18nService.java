package controllers;

import jsmessages.JsMessages;
import jsmessages.JsMessagesFactory;

import javax.inject.Inject;
import java.util.Map;

import static scala.collection.JavaConversions.mapAsJavaMap;

/**
 * Created by gbecan on 6/26/15.
 */
public class I18nService {

    @Inject
    private JsMessagesFactory jsMessagesFactory;
    private final JsMessages jsMessages = jsMessagesFactory.all();

    public Map<String, String> getMessages(String language) {
        return mapAsJavaMap(jsMessages.allMessages().apply(language));
    }



}
