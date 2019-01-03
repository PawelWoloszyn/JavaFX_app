package com.woloszyn.studyApp.dataModel;

// TODO: 27.12.18 Make queries in a Map

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import java.io.*;
import java.util.Set;
import java.util.TreeSet;


public class EventData {

    private static EventData instance = new EventData();

    private static String fileName1 = "events.xml";

    private static String fileName2 = "categories.xml";

    private ObservableList<ImportantEvent> importantEvents;
    private Set<Category> categories;

    public static EventData getInstance() {
        return instance;
    }

    public void addImportantEvent(ImportantEvent event) {
        importantEvents.add(event);
    }

    public void updateImportantEvent(ImportantEvent oldEvent, ImportantEvent newEvent) {
        importantEvents.set(importantEvents.indexOf(oldEvent), newEvent);
    }


    public void deleteImportantEvent(ImportantEvent event) {
        importantEvents.remove(event);

    }

    public Set<Category> getCategories() {
        return categories;
    }

    public ObservableList<ImportantEvent> getImportantEvents() {
        return importantEvents;
    }

    public boolean checkIfExists(ImportantEvent checkedEvent) {
        for (ImportantEvent event : importantEvents) {
            if (event.equals(checkedEvent)) {
                System.out.println("compared: " + checkedEvent + " is equal to found: "+ event);
                return true;
            }
        }
        return false;
    }

    public void loadEvents() {
        importantEvents = FXCollections.observableArrayList();
        try {
            SAXBuilder builder = new SAXBuilder();
            Document readDocument = builder.build(new File(fileName1));

            Element root = readDocument.getRootElement();

            for (Element currentEvent : root.getChildren("ImportantEvent")) {
                String name = currentEvent.getChildText("name");
                String description = currentEvent.getChildText("description");
                String categoryMessage = currentEvent.getChildText("category");

                Category newCategory = new Category(categoryMessage);
                ImportantEvent newEvent = new ImportantEvent(name, description, newCategory);
                importantEvents.add(newEvent);
                String attribute = currentEvent.getChild("queries").getAttributeValue("isEmpty");
                if (attribute.equals("no")) {
                    Element queries = currentEvent.getChild("queries");
                    for (Element query : queries.getChildren("query")) {
                        String question = query.getChildText("question");
                        String answer = query.getChildText("answer");
                        newEvent.addQuery(question, answer);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void saveEvents() {
        try {
            Document document = new Document();

            Element theRoot = new Element("ImportantEventData");
            document.setRootElement(theRoot);

            for (ImportantEvent event : importantEvents) {
                // initializing elements
                Element importantEvent = new Element("ImportantEvent");
                Element name = new Element("name");
                Element description = new Element("description");
                Element category = new Element("category");
                Element queries = new Element("queries");
                //initialize contents
                name.addContent(event.getName());
                description.addContent(event.getDescription());
                category.addContent(event.getCategory().toString());
                if (event.getQueries().size() > 0) {
                    queries.setAttribute("isEmpty", "no");
                    for (Query currentQuery : event.getQueries()) {
                        Element query = new Element("query");
                        Element question = new Element("question");
                        Element answer = new Element("answer");

                        question.addContent(currentQuery.getQuestion());
                        answer.addContent(currentQuery.getAnswer());
                        query.addContent(question);
                        query.addContent(answer);
                        queries.addContent(query);
                    }
                } else {
                    queries.setAttribute("isEmpty", "yes");
                }
                importantEvent.addContent(name);
                importantEvent.addContent(description);
                importantEvent.addContent(category);
                importantEvent.addContent(queries);
                theRoot.addContent(importantEvent);
            }

            XMLOutputter xmlOutput = new XMLOutputter(Format.getPrettyFormat());
            xmlOutput.output(document, new FileOutputStream(new File(fileName1)));

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void loadCategories() {
        categories = new TreeSet<Category>();
        try {
            SAXBuilder builder = new SAXBuilder();
            Document readDocument = builder.build(new File(fileName2));
            Element root = readDocument.getRootElement();
            for (Element currentEvent : root.getChildren("category")) {
                String name = currentEvent.getText();
                Category newCategory = new Category(name);
                categories.add(newCategory);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void saveCategories() {
        try {
            Document document = new Document();
            Element theRoot = new Element("Categories");
            document.setRootElement(theRoot);
            for (Category currentCategory : categories) {
                Element newCategory = new Element("category");
                newCategory.addContent(currentCategory.toString());
                theRoot.addContent(newCategory);
            }

            XMLOutputter xmlOutput = new XMLOutputter(Format.getPrettyFormat());
            xmlOutput.output(document, new FileOutputStream(new File(fileName2)));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
