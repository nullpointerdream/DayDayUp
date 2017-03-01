## dom4j快速入门
[参考自此处](http://dom4j.sourceforge.net/dom4j-1.6.1/guide.html)

### 解析XMl
用dom4j解析某种xml文件很简单，示例代码：

	import java.net.URL;
	
	import org.dom4j.Document;
	import org.dom4j.DocumentException;
	import org.dom4j.io.SAXReader;
	
	public class Foo {
	
	    public Document parse(URL url) throws DocumentException {
	        SAXReader reader = new SAXReader();
	        Document document = reader.read(url);
	        return document;
	    }
	}

reader.read有很多重载方法。都是读取xml文件，转为document。


### 使用迭代器
document使用标准的java迭代器返回子元素，然后进行各种操作。

	public void bar(Document document) throws DocumentException {
	
	        Element root = document.getRootElement();
	
	        // iterate through child elements of root
	        for ( Iterator i = root.elementIterator(); i.hasNext(); ) {
	            Element element = (Element) i.next();
	            // do something
	        }
	
	        // iterate through child elements of root with element name "foo"
	        for ( Iterator i = root.elementIterator( "foo" ); i.hasNext(); ) {
	            Element foo = (Element) i.next();
	            // do something
	        }
	
	        // iterate through attributes of root 
	        for ( Iterator i = root.attributeIterator(); i.hasNext(); ) {
	            Attribute attribute = (Attribute) i.next();
	            // do something
	        }
	     }

### 使用xpath进行强大的定位
xpath表达式可以在dom4j的document及其任何节点中执行，这样可以进行复杂的节点定位。

	public void bar(Document document) {
			// 直接找到foo下的bar节点list，而不需要一层层的迭代器循环
	        List list = document.selectNodes( "//foo/bar" );
	
			// 直接找到某个节点
	        Node node = document.selectSingleNode( "//foo/bar/author" );
	
	        String name = node.valueOf( "@name" );
	    }

如想在XHTML文档中找出所有的超链接，可以这样做：

    public void findLinks(Document document) throws DocumentException {

        List list = document.selectNodes( "//a/@href" );

        for (Iterator iter = list.iterator(); iter.hasNext(); ) {
            Attribute attribute = (Attribute) iter.next();
            String url = attribute.getValue();
        }
    }

对于xpath的用法，请参考[此处](http://www.zvon.org/xxl/XPathTutorial/General/examples.html)

### 快速循环
如果循环一个非常大的文档有性能问题，建议使用下面这种快速循环，已避免创建迭代器的消耗。  

	public void treeWalk(Document document) {
        treeWalk( document.getRootElement() );
    }

    public void treeWalk(Element element) {
        for ( int i = 0, size = element.nodeCount(); i < size; i++ ) {
            Node node = element.node(i);
            if ( node instanceof Element ) {
                treeWalk( (Element) node );
            }
            else {
                // do something....
            }
        }
    }

其实是使用递归，以空间换时间。

### 创建document
有时可能需要创建一个document，以保存抓出来的数据。

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

	public class Foo {

	    public Document createDocument() {
	        Document document = DocumentHelper.createDocument();
	        Element root = document.addElement( "root" );
	
	        Element author1 = root.addElement( "author" )
	            .addAttribute( "name", "James" )
	            .addAttribute( "location", "UK" )
	            .addText( "James Strachan" );
	        
	        Element author2 = root.addElement( "author" )
	            .addAttribute( "name", "Bob" )
	            .addAttribute( "location", "US" )
	            .addText( "Bob McWhirter" );
	
	        return document;
	    }
	}

### 将document写入文件
你创建了一个document，然后想持久化保存。  
最简单快捷的方法是使用document(或者节点)的write()方法。

	  FileWriter out = new FileWriter( "foo.xml" );
	  document.write( out );

但是如果你想要控制输出的格式，或者想用Writer/OutputStream作为目标而不是文件，你可以使用XMLWriter类。

	import org.dom4j.Document;
	import org.dom4j.io.OutputFormat;
	import org.dom4j.io.XMLWriter;
	
	public class Foo {
	
	    public void write(Document document) throws IOException {
	
	        // lets write to a file
	        XMLWriter writer = new XMLWriter(
	            new FileWriter( "output.xml" )
	        );
	        writer.write( document );
	        writer.close();
	
	
	        // Pretty print the document to System.out
	        OutputFormat format = OutputFormat.createPrettyPrint();
	        writer = new XMLWriter( System.out, format );
	        writer.write( document );
	
	        // Compact format to System.out
	        format = OutputFormat.createCompactFormat();
	        writer = new XMLWriter( System.out, format );
	        writer.write( document );
	    }
	}

### 和字符创的转换
document/element转成string

		Document document = ...;
        String text = document.asXML();

xml格式的string转成document

        String text = "<person> <name>James</name> </person>";
        Document document = DocumentHelper.parseText(text);

### 使用XSLT对document进行格式转换
XSLT即将XML格式的文档转为其他格式，如XHTML。比较直接的方式是使用JAXP的api。然后你可以使用XSLT转换引擎，如Xalan或SAXON。下面的例子是使用JAXP去创建一个转换器，然后转换document。

	import javax.xml.transform.Transformer;
	import javax.xml.transform.TransformerFactory;
	
	import org.dom4j.Document;
	import org.dom4j.io.DocumentResult;
	import org.dom4j.io.DocumentSource;
	
	public class Foo {
	
	    public Document styleDocument(Document document, String styleshee) throws Exception 
		{
	
	        // load the transformer using JAXP
	        TransformerFactory factory = TransformerFactory.newInstance();
	        Transformer transformer = factory.newTransformer( 
	            new StreamSource( stylesheet ) 
	        );
	
	        // now lets style the given document
	        DocumentSource source = new DocumentSource( document );
	        DocumentResult result = new DocumentResult();
	        transformer.transform( source, result );
	
	        // return the transformed document
	        Document transformedDoc = result.getDocument();
	        return transformedDoc;
	    }
	}
