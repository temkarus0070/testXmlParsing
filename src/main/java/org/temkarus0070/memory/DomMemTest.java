package org.temkarus0070.memory;

import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.Set;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class DomMemTest implements MemoryTest {
    private  long result=-1;
    @Override
    public void doTest(InputStream file, Set<String> searchedValues, String tagToCalcCount) throws ParserConfigurationException {
        try (file) {
            long begin=System.nanoTime();
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = factory.newDocumentBuilder();
            Document parse = documentBuilder.parse(file);
            testAllElementsCheckAndTag(searchedValues,parse.getDocumentElement(),tagToCalcCount);
        long end=System.nanoTime();
        result=end-begin;
        } catch (IOException | SAXException e) {
            e.printStackTrace();
        }
    }

    private void testAllElementsCheckAndTag(Set<String> searchedValues, Node root,String tag) {
        int tagCount=0;
        NodeList nodeListWithRoot = new NodeList() {
            @Override
            public Node item(int index) {
                return root;
            }

            @Override
            public int getLength() {
                return 1;
            }
        };
        LinkedList<NodeList> stack = new LinkedList<>();
        stack.add(nodeListWithRoot);
        int valueCount=0;
        while (!stack.isEmpty()) {
            NodeList last = stack.removeLast();
            int length = last.getLength();
            for (int i = 0; i < length; i++) {
                Node element = last.item(i);
                String nodeName = element.getNodeName();
                int prefixIndex = nodeName.indexOf(":");
                nodeName = nodeName.substring(prefixIndex == -1 ? 0 : prefixIndex + 1);
                if (nodeName.equals(tag)) {
                    tagCount++;
                }
                if (element.getNodeValue() != null && searchedValues.contains(element.getNodeValue())) {
                    valueCount++;
                }
                NodeList childNodes = element.getChildNodes();
                if (childNodes.getLength() > 0)
                    stack.add(childNodes);
            }
        }
        System.out.println(valueCount);
        System.out.println(tagCount);
    }

    @Override
    public long getTestResult() throws IllegalArgumentException {
        if (result==-1)
           throw  new IllegalArgumentException();
        return result;
    }
}
