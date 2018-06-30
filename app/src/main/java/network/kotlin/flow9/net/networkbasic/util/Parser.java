package network.kotlin.flow9.net.networkbasic.util;

import android.sax.RootElement;
import android.sax.StartElementListener;
import android.util.Log;
import android.util.Xml;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

public class Parser {

    public static String XML = "<?xml version=\"1.0\" encoding=\"utf-8\"?> \n"+
                                    "<order>"+
                                        "<item>Mouse</item>"+
                                    "</order>";

    public static String XML2 = "<?xml version=\"1.0\" encoding=\"utf-8\"?> \n"+
                                    "<order>"+
                                        "<item Maker=\"Samsung\" Price=\"23000\">Mouse</item>"+
                                        "<item Maker=\"LG\" Price=\"12000\">KeyBoard</item>"+
                                        "<item Maker=\"Western Digital\" Price=\"156000\">HDD</item>"+
                                    "</order>";

    public static String DomParseSingle(String xml) {
        String itemName = "";
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            InputStream is = new ByteArrayInputStream(xml.getBytes("utf-8"));
            Document doc = builder.parse(is);
            Element order = doc.getDocumentElement();
            NodeList items = order.getElementsByTagName("item");
            Node item = items.item(0);
            Node text = item.getFirstChild();
            itemName = text.getNodeValue();
        } catch (ParserConfigurationException e) {

        } catch (UnsupportedEncodingException e) {

        } catch (SAXException e) {

        } catch (IOException e) {

        }
        return itemName;
    }

    public static String DomParseMultiple(String xml) {
        String result = "";
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            InputStream is = new ByteArrayInputStream(xml.getBytes("utf-8"));
            Document doc = builder.parse(is);
            Element order = doc.getDocumentElement();
            NodeList items = order.getElementsByTagName("item");
            for (int i = 0; i < items.getLength(); i++) {
                Node item = items.item(i);
                Node text = item.getFirstChild();
                String itemName = text.getNodeValue();
                result += itemName + " : ";

                NamedNodeMap Attrs = item.getAttributes();
                for (int j = 0; j < Attrs.getLength(); j++) {
                    Node attr = Attrs.item(j);
                    result += attr.getNodeName() + " = " + attr.getNodeValue() + " ";
                }
                result += "\n";
            }
        } catch (ParserConfigurationException e) {

        } catch (UnsupportedEncodingException e) {

        } catch (SAXException e) {

        } catch (IOException e) {

        }
        return result;
    }

    public static String SAXParse(String xml) {
        String result = "";
        try {
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser parser = factory.newSAXParser();
            XMLReader reader = parser.getXMLReader();
            SaxHandler handler = new SaxHandler();
            reader.setContentHandler(handler);
            InputStream is = new ByteArrayInputStream(xml.getBytes("utf-8"));
            reader.parse(new InputSource(is));
            result += handler.item;
        } catch (ParserConfigurationException e) {

        } catch (SAXException e) {

        } catch (UnsupportedEncodingException e) {

        } catch (IOException e) {

        }
        return result;
    }

    static class SaxHandler extends DefaultHandler {

        boolean initem = false;
        StringBuilder item = new StringBuilder();

        @Override
        public void startDocument() throws SAXException {

        }

        @Override
        public void endDocument() throws SAXException {

        }

        @Override
        public void startElement(String uri, String localName,
                                 String qName, Attributes attributes) throws SAXException {
            if (localName.equals("item")) {
                initem = true;
            }
        }

        @Override
        public void endElement(String uri, String localName, String qName) throws SAXException {

        }

        @Override
        public void characters(char[] ch, int start, int length) throws SAXException {
            if (initem) {
                item.append(ch, start, length);
                initem = false;
            }
        }
    }

    public static void SAXParseTwo(String xml) {
        RootElement root = new RootElement("current");
        android.sax.Element cityElement = root.getChild("city");
        android.sax.Element tempElement = root.getChild("temperature");

        cityElement.setStartElementListener(new StartElementListener() {
            @Override
            public void start(Attributes attributes) {
                Log.d("SAXParseTwo", attributes.getValue("name"));
            }
        });
        tempElement.setStartElementListener(new StartElementListener() {
            @Override
            public void start(Attributes attributes) {
                Log.d("SAXParseTwo", attributes.getValue("name"));
            }
        });

        try {
            InputStream is = new ByteArrayInputStream(xml.getBytes("utf-8"));
            Xml.parse(is, Xml.Encoding.UTF_8, root.getContentHandler());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String XMLPullParse(String xml) {
        String result = "";
        boolean initem = false;
        String itemName = "";
        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser parser = factory.newPullParser();
            parser.setInput(new StringReader(xml));
            // 현재 사건을 조사하며 next 메서드로 다음 사건을 조사하면서 문서를 처음부터 순회한다
            int eventType = parser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                switch (eventType) {
                    case XmlPullParser.START_DOCUMENT:
                        break;
                    case XmlPullParser.END_DOCUMENT:
                        break;
                    case XmlPullParser.START_TAG:
                        // 최초 문서 시작 이벤트에서 시작하여 문서가 끝날 때까지 태그나 텍스트를 만나면 각 사건마다 getName
                        // getText 메서드로 태그 및 텍스트 내용을 조사하여 원하는 정보를 추출한
                        if (parser.getName().equals("item")) {
                            initem = true;
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        break;
                    case XmlPullParser.TEXT:
                        if (initem) {
                            itemName = parser.getText();
                            initem = false;
                        }
                        break;
                }
                eventType = parser.next();
            }
            result += itemName;
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static void XMLPullParseTwo(String xml) {

        try {
            InputStream is = new ByteArrayInputStream(xml.getBytes("utf-8"));
            XmlPullParser parser = Xml.newPullParser();
            parser.setInput(is, null);
            int eventType = parser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                String name = null;
                if (eventType == XmlPullParser.START_TAG) {
                    name = parser.getName();
                    if (name.equals("city")) {

                    } else if (name.equals("temperature")) {

                    }
                }
                eventType = parser.next();

            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String JsonParseArray(String json) {
        String result = "";
        try {
            int sum = 0;
            JSONArray ja = new JSONArray(json);
            for (int i = 0; i < ja.length(); i++) {
                sum += ja.getInt(i);
            }
            result += sum+"";
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static String JsonParseSimple(String json) {
        String result = "";
        try {
            JSONArray ja = new JSONArray(json);
            for (int i = 0; i < ja.length(); i++) {
                JSONObject order = ja.getJSONObject(i);
                result += "제품명 : " + order.getString("Product");
                result += ",제조사 : " + order.getString("Maker");
                result += ",가격 : " + order.getInt("Price");
                result += "\n";
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }

}
