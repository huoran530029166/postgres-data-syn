package com.goldwind.dataaccess.file;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import com.alibaba.fastjson.JSONObject;

/**
 * XML文件操作
 * 
 * @author 曹阳
 *
 */
public class XMLFileOper
{
    /**
     * 配置文件集合
     */
    // private static HashMap<String, Document> XMLFILES = new HashMap<String, Document>();
    /**
     * 配置文件集合
     */
    private Document xmlFile;
    /**
     * 文件路径
     */
    private String filePath;

    public XMLFileOper(String filePath) throws DocumentException
    {
        this.filePath = filePath;
        File inputXml = new File(filePath);
        SAXReader saxReader = new SAXReader();
        xmlFile = saxReader.read(inputXml);
    }

    /**
     * 设置键值
     * 
     * @param parameters
     *            依次传入键的父节点，最后一个为键值
     * @throws IOException
     *             i/o异常
     */
    public synchronized void setProperty(String... parameters) throws IOException
    {
        Element root = xmlFile.getRootElement();
        Element tmp = null;
        for (int i = 0; i < parameters.length; i++)
        {
            if (tmp == null)
            {
                tmp = addElement(root, parameters[i]);
            }
            else
            {
                if (i == parameters.length - 1)
                {
                    tmp.setText(parameters[i]);
                }
                else
                {
                    tmp = addElement(tmp, parameters[i]);
                }
            }

        }
        XMLWriter pw = new XMLWriter(new FileWriter(getFilePath()));
        pw.write(xmlFile);
        pw.close();
    }

    /**
     * 读取键值
     * 
     * @param parameters
     *            依次传入该键的父节点
     * @return 值
     */
    public String getProperty(String... parameters)
    {
        Element root = xmlFile.getRootElement();
        String val = null;
        Element element = null;
        for (int i = 0; i < parameters.length; i++)
        {
            if (i == 0)
            {
                element = root.element(parameters[i]);
            }
            else
            {
                if (element != null)
                {
                    element = element.element(parameters[i]);
                }
            }
            if (i == parameters.length - 1 && element != null)
            {
                val = element.getText();
            }
        }
        return val;
    }

    /**
     * 读取指定属性值
     * 
     * @param xPath
     *            路徑
     * @param attributeName
     *            属性名称
     * @return 值
     */
    public String getAttribute(String xPath, String attributeName)
    {
        String val = null;
        Element element = (Element) xmlFile.selectSingleNode(xPath);
        if (element != null)
        {
            if (attributeName == null || ("").equals(attributeName.trim()))
            {
                val = element.getText();
            }
            else
            {
                val = element.attributeValue(attributeName);
            }
        }
        return val;
    }

    /**
     * 读取键值
     * 
     * @param parameters
     *            依次传入该键的父节点
     * @return 节点下所有值
     */
    public List<String> getProperties(String... parameters)
    {
        List<String> values = new ArrayList<>();
        Element root = xmlFile.getRootElement();
        Element e = null;
        for (int i = 0; i < parameters.length; i++)
        {
            if (i == 0)
            {
                e = root.element(parameters[i]);
            }
            else
            {
                if (e != null)
                {
                    e = e.element(parameters[i]);
                }
            }
            if (e != null && i == parameters.length - 1)
            {
                List<Element> tmp = e.elements();
                if (!tmp.isEmpty())
                {
                    for (Element t : tmp)
                    {
                        values.add(t.getText());
                    }
                }
                else
                {
                    values.add(e.getText());
                }
            }
        }
        return values;
    }

    /**
     * 增加元素
     * 
     * @param parent
     *            父元素
     * @param child
     *            子元素名
     * @return 子元素对象
     */
    private static Element addElement(Element parent, String child)
    {
        Element tmp = parent.element(child);
        if (tmp == null)
        {
            parent.addElement(child);
        }
        return parent.element(child);
    }

    /**
     * @return the filePath
     */
    public String getFilePath()
    {
        return filePath;
    }

    /**
     * xml元素转json列表
     * 
     * @param isroot
     *            是否根节点
     * @param parameters
     *            依次传入该键的父节点
     * @return json对象列表
     */
    public List<JSONObject> element2Json(boolean isroot, String... parameters)
    {
        List<JSONObject> oList = new ArrayList<>();
        Element root = xmlFile.getRootElement();
        Element element = null;
        if (isroot)
        {
            element = root;
        }
        else
        {
            if (parameters != null && parameters.length > 0)
            {
                for (int i = 0; i < parameters.length; i++)
                {
                    if (i == 0)
                    {
                        element = root.element(parameters[i]);
                    }
                    else
                    {
                        if (element != null)
                        {
                            element = element.element(parameters[i]);
                        }
                    }
                }
            }
        }
        if (element != null)
        {
            List<Element> eList = element.elements();
            for (Element e : eList)
            {
                JSONObject jObject = new JSONObject();
                List<Attribute> aList = e.attributes();
                for (Attribute a : aList)
                {
                    jObject.put(a.getName(), a.getValue());
                }
                oList.add(jObject);
            }
        }
        return oList;
    }

    /**
     * 获得所有的节点名和对应的节点内容
     * 
     * @return map
     */
    public Map<String, String> getAllnodes()
    {
        Element root = xmlFile.getRootElement();
        Map<String, String> allNodesName = new HashMap<>();
        String nodesName = "";
        allNodesName = getNodes(nodesName, allNodesName, root);
        return allNodesName;
    }

    /**
     * 从指定的节点（根节点）开始,递归遍历所有子节点 ，并且装入到传入的Map键值对中
     * 
     * @author libo
     * 
     * @param nodesName
     *            是传入的空值
     * 
     * @param allnodesName
     *            是传入的xml文件节点名和节点内容的键值对
     * 
     * @param node
     *            是传入的根节点，固定值
     * @return map
     */

    public static Map<String, String> getNodes(String nodesName, Map<String, String> allnodesName, Element node)
    { // node是根节点，nodeName是空的值
      // System.out.println("--------------------");
        String nodes = nodesName + "." + node.getName();
        // 当前节点的处理
        if (node.elements().isEmpty())
        {
            String nodevalue = node.getText();
            String[] spnode = nodes.split("\\.");
            String newnodes = nodes.replace(spnode[0] + "." + spnode[1] + ".", "");
            allnodesName.put(newnodes, nodevalue);
            // System.out.println(newnodes);
        }

        // 递归遍历当前节点所有的子节点
        List<Element> listElement = node.elements();
        for (Element e : listElement)
        {
            getNodes(nodes, allnodesName, e);
        }
        return allnodesName;
    }

    /**
     * 获取根节点下的所以元素
     * 
     * @return 根节点下的元素
     */
    public Element getElement()
    {
        return xmlFile.getRootElement();
    }

    /**
     * 获取指定元素
     * 
     * @param parameters
     *            依次传入该键的父节点
     * @return 指定元素
     */
    public Element getElement(String... parameters)
    {
        Element root = xmlFile.getRootElement();
        Element val = null;
        Element element = null;
        for (int i = 0; i < parameters.length; i++)
        {
            if (i == 0)
            {
                element = root.element(parameters[i]);
            }
            else
            {
                if (element != null)
                {
                    element = element.element(parameters[i]);
                }
            }
            if (i == parameters.length - 1)
            {
                val = element;
            }
        }
        return val;
    }

}
