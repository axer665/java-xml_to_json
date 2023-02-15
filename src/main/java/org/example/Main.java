package org.example;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        List<Employee> list = parseXML("data.xml");
        String json = listToJson(list);
        writeString(json);
    }

    private static List<Employee> parseXML(String xmlFile) {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        List<Employee> employees = new ArrayList<>();
        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(
                    new File("data.xml")
            );

            Node root = doc.getDocumentElement();
            NodeList nodeList = root.getChildNodes();

            for (int i = 0; i < nodeList.getLength(); i++) {
                Employee employee = new Employee();
                Node node = nodeList.item(i);
                NodeList emp = node.getChildNodes();
                boolean addEmployee = false;

                for (int j = 0; j < emp.getLength(); j++) {
                    Node node_ = emp.item(j);
                    if (Node.ELEMENT_NODE == node_.getNodeType()) {
                        addEmployee = true;
                        switch (node_.getNodeName()) {
                            case "id" :
                                employee.id = Long.parseLong(node_.getTextContent(), 10);
                                break;
                            case "firstName" :
                                employee.firstName = node_.getTextContent();
                                break;
                            case "lastName" :
                                employee.lastName = node_.getTextContent();
                                break;
                            case "counry" :
                                employee.country = node_.getTextContent();
                                break;
                            case "age" :
                                employee.age = Integer.parseInt(node_.getTextContent());;
                        }

                    }
                }
                if (addEmployee)
                    employees.add(employee);
            }

        } catch (ParserConfigurationException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (SAXException e) {
            throw new RuntimeException(e);
        }

        return employees;
    }

    private static String listToJson(List<Employee> list) {
        Type listType = new TypeToken<List<Employee>>() {}.getType();
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        String json = gson.toJson(list, listType);
        return json;
    }

    private static void writeString(String json) {
        try (FileWriter file = new FileWriter("data.json")) {
            file.write(json);
            file.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}