package klusman.scaantirevents.mobile.Tasks;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;


import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import klusman.scaantirevents.mobile.MyApp;
import klusman.scaantirevents.mobile.Objects.Event;

public class JsonHelper
{
    static private Type EventList = new TypeToken<ArrayList<Event>>()
    {}.getType();


    private static JsonReader getReader(String s)
    {
        return new JsonReader(new StringReader(s));
    }


    public static List<Event> parseEventList(String s) throws IOException
    {
        JsonReader reader = getReader(s);
//        Gson gson = new GsonBuilder()
//                .registerTypeAdapter(EventList, new EventAdapter())
//                .create();
       // List<Event> r = gson.fromJson(reader, EventList);
        reader.close();
        return null;
    }


    private static class CompanyAdapter implements JsonDeserializer<List<Event>>
    {

        @Override
        public List<Event> deserialize(JsonElement element, Type arg1, JsonDeserializationContext ctx)
                throws JsonParseException {
            Set<Entry<String, JsonElement>> all = element.getAsJsonObject().entrySet();
            List<Event> response = new ArrayList<Event>();
            for (Entry<String, JsonElement> e : all) {
                Event company = ctx.deserialize(e.getValue(), Event.class);
                response.add(company);
            }
            MyApp.getInstance().getDaoSession().getEventDao().insertOrReplaceInTx(response);
            return response;
        }
    }




    public static class JsonExtractor
    {

        private static final String STRING = "string";

        private SAXParser parser;

        public JsonExtractor()
        {

            SAXParserFactory factory = SAXParserFactory.newInstance();
            try
            {
                parser = factory.newSAXParser();
            }
            catch (ParserConfigurationException e)
            {
                e.printStackTrace();
            }
            catch (SAXException e)
            {
                e.printStackTrace();
            }
        }

        public String parse(InputStream is) throws SAXException, IOException
        {

            MyHandler handler = new MyHandler();
            parser.parse(is, handler);
            return handler.res;
        }

        class MyHandler extends DefaultHandler
        {

            private String res;
            private StringBuilder builder = new StringBuilder();

            @Override
            public void endElement(String uri, String localName, String qName) throws SAXException
            {
                super.endElement(uri, localName, qName);

                if (STRING.equalsIgnoreCase(localName))
                    res = builder.toString().trim();
                builder.setLength(0);
            }

            @Override
            public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException
            {

                super.startElement(uri, localName, qName, attributes);
            }

            @Override
            public void characters(char[] ch, int start, int length) throws SAXException
            {

                super.characters(ch, start, length);
                builder.append(ch, start, length);
            }
        }
    }

}
