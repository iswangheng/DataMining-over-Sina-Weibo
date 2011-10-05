/* 
 * Copyright 2002-2009 Andy Clark, Marc Guillemot
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package swarm;

import java.io.BufferedReader;
import java.io.FileReader;

import org.apache.html.dom.HTMLDocumentImpl;
import org.cyberneko.html.parsers.DOMFragmentParser;
import org.cyberneko.html.parsers.DOMParser;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.html.HTMLDocument;
import org.xml.sax.InputSource;

/**
 * This program tests the NekoHTML parser's use of the HTML DOM
 * implementation by printing the class names of all the nodes in
 * the parsed document.
 *
 * @author Andy Clark
 *
 * @version $Id: TestHTMLDOM.java,v 1.3 2004/02/19 20:00:17 andyc Exp $
 */
public class TestHTMLDOM {

	public static String TextExtractor(Node root){  
		   //若是文本节点的话，直接返回  
		   if (root.getNodeType() == Node.TEXT_NODE) {  
		    return root.getNodeValue().trim();  
		   }  
		   if(root.getNodeType() == Node.ELEMENT_NODE) {  
		    Element elmt = (Element) root;  
		    //抛弃脚本  
		    if (elmt.getTagName().equals("STYLE")  
		      || elmt.getTagName().equals("SCRIPT"))  
		     return "";  
		     
		    NodeList children = elmt.getChildNodes();  
		    StringBuilder text = new StringBuilder();  
		    for (int i = 0; i < children.getLength(); i++) {  
		     text.append(TextExtractor(children.item(i)));  
		    }  
		    return text.toString();  
		   }  
		   //对其它类型的节点，返回空值  
		   return "";  
		}  
		public static void main(String[] args) throws Exception{  
		   //生成html parser  
		   DOMParser parser = new DOMParser();  
		   //设置网页的默认编码  
		   //parser.setProperty( "http://cyberneko.org/html/properties/default-encoding", "utf-8");  
		   //input file  
		   BufferedReader in = new BufferedReader(new FileReader("Google.htm"));  
		   parser.parse(new InputSource(in));  
		   Document doc = parser.getDocument();  
		   //获得body节点，以此为根，计算其文本内容  
		   Node body = doc.getElementsByTagName("BODY").item(0);  
		   System.out.println(TextExtractor(body));  
		} 
	} 