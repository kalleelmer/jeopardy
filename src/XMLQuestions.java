import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import java.io.File;

public class XMLQuestions {

	public static Category[] getCategoryList(File fXmlFile) {

		try {

			DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
			Document doc = docBuilder.parse(fXmlFile);

			// normalize text representation
			doc.getDocumentElement().normalize();
			System.out.println("XML-dokumentets rot är: '"
					+ doc.getDocumentElement().getNodeName() + "'");

			NodeList listOfCategories = doc.getElementsByTagName("category");
			int nbrOfCategories = listOfCategories.getLength();
			System.out.println("Hittade " + nbrOfCategories + " kategorier.\n");
			Category[] categories = new Category[listOfCategories.getLength()];

			for (int c = 0; c < listOfCategories.getLength(); c++) {
				Node categoryNode = listOfCategories.item(c);
				if (categoryNode.getNodeType() == Node.ELEMENT_NODE) {
					Element categoryElement = (Element) categoryNode;
					// Hämta kategorins titel
					Node titleElement = categoryElement.getElementsByTagName(
							"title").item(0);
					Node titleItem = titleElement.getChildNodes().item(0);
					if (titleItem == null) {
						System.out.println("Kategori null");
						categories[c] = null;
						continue;
					}
					String titleValue = titleItem.getNodeValue().trim();
					System.out.println(titleValue);

					// Hämta lista på frågor
					NodeList questionList = categoryElement
							.getElementsByTagName("question");
					Question[] questions = new Question[5];

					for (int q = 0; q < 5; q++) {
						Node questionNode = questionList.item(q);
						if (questionNode.getNodeType() == Node.ELEMENT_NODE) {
							Element questionElement = (Element) questionNode;
							// Hämta frågans text
							Node textNode = questionElement
									.getElementsByTagName("text").item(0);
							Node textItem = textNode.getChildNodes().item(0);
							if (textItem == null) {
								System.out.println("\tFråga null");
								questions[q] = null;
								continue;
							}
							String textValue = textItem.getNodeValue().trim();
							System.out.println("\tFråga: " + textValue);
							// Hämta frågans text
							Element answerElement = (Element) questionElement
									.getElementsByTagName("answer").item(0);
							String answerValue = (((Node) answerElement
									.getChildNodes().item(0)).getNodeValue()
									.trim());
							System.out.println("\t Svar: " + answerValue);
							// Spara värdet i en fråga
							questions[q] = new Question(textValue, answerValue);
						}
					}// Slut frågeloop
					categories[c] = new Category(titleValue, questions);
				}
			}// Slut kategoriloop
			return categories;
		} catch (SAXParseException err) {
			System.out.println("** Parsing error" + ", line "
					+ err.getLineNumber() + ", uri " + err.getSystemId());
			System.out.println(" " + err.getMessage());

		} catch (SAXException e) {
			Exception x = e.getException();
			((x == null) ? e : x).printStackTrace();

		} catch (Throwable t) {
			t.printStackTrace();
		}
		return null;
	}
}