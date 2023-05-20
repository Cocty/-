package huffmancode;
import java.io.*;
import java.util.*;
//�ļ�ѹ��
public class Compress {
	    public static void main(String[] args) {
	    	System.out.println("ѹ���ļ���");
	        String zipFile = "D:\\BaiduNetdiskDownload\\����������\\huffmancode\\������������豨��.docx";
	        String dstFile = "D:\\BaiduNetdiskDownload\\����������\\huffmancode\\AfterDepressing.code";
	        zipFile(zipFile, dstFile);
	        System.out.println("ѹ���ɹ�!");
	    }
	    
	    static Map<Byte, String> huffmanCodes = new HashMap<Byte, String>();//�����������
	    static Node huffmantree;
	    static int endLen;//��¼���һ���ֽڵĶ����ƴ��ĳ���
	    static int wpl;
	    
	    public static void zipFile(String srcFile, String dstFile) {
	        OutputStream os = null;
	        ObjectOutputStream oos = null;
	        FileInputStream is = null;
	        try {
	            is = new FileInputStream(srcFile);
	            byte[] b = new byte[is.available()];
	            is.read(b);
	            byte[] huffmanBytes = huffmanZip(b);
	            os = new FileOutputStream(dstFile);
	            oos = new ObjectOutputStream(os);
	            //�������л�
	            oos.writeObject(huffmanBytes);//���������ֽ���������ļ�
	            oos.writeObject(huffmanCodes);//����������Ҳ�����ļ�
	            oos.writeObject(endLen);
	            System.out.println("ѹ����Ϊ");
	            System.out.println((double)huffmanBytes.length/b.length);
	            System.out.println("WplΪ��");
		        System.out.println(Wpl(huffmantree));
	            System.out.println("�����������Ϊ��");
	            for (Map.Entry<Byte,String > entry : huffmanCodes.entrySet()) {
					System.out.println(entry.getKey()+":"+entry.getValue());
				}
	            System.out.println();
//	            System.out.println("����������Ϊ��");
//	            int count = 0;
//	            for (byte a : b) {
//					System.out.print(huffmanCodes.get(a));
//					count++;
//					while(count>100) {
//						System.out.println();
//						count = 0;//ʹ��ӡ�����ı�����ӵ�����
//					}
//				}
//	            System.out.println();
	 
	        } catch (Exception e) {
	            System.out.println(e.getMessage());
	        } finally {
	            try {
	                is.close();
	                oos.close();
	                os.close();
	            } catch (Exception e) {
	                System.out.println(e.getMessage());
	            }
	        }
	    }

	    //����������ѹ��
	     static byte[] huffmanZip(byte[] bytes) {
	        List<Node> nodes = getNodes(bytes);
	        //��������
	        huffmantree = createHuffmanTree(nodes);
	        //�����������
	        Map<Byte, String> huffmanCodes = getCodes(huffmantree);
	        
	        byte[] zip = zip(bytes, huffmanCodes);
	        return zip;
	    }

	    //ѹ��
	     static byte[] zip(byte[] bytes, Map<Byte, String> huffmanCodes) {
	        StringBuilder stringBuilder = new StringBuilder();
	        for (byte b : bytes) {
				stringBuilder.append(huffmanCodes.get(b));
			}
	        int len;
	        if (stringBuilder.length() % 8 == 0) {//������볤���ǰ˵ı���
	            len = stringBuilder.length() / 8;//���ֽ�����ĳ���Ϊ���������볤��/8
	        } else {
	            len = stringBuilder.length() / 8 + 1;//������������һ���ֽ���һ���ֽ�����
	        }
	        endLen = stringBuilder.length()%8;
	        byte[] by = new byte[len];
	        int index = 0;
	        for (int i = 0; i < stringBuilder.length(); i += 8) {
	            String strByte;
	            if (i + 8 > stringBuilder.length()) {//����������һ����벻��8λ
	                strByte = stringBuilder.substring(i);//��ȡʣ�µı���
	                by[index] = (byte) Integer.parseInt(strByte, 2);//�������Ƶ�strByte�ַ���ת��Ϊʮ���Ƶ��ֽ�
	                index++;
	            } else {//�����û�е����һ��,��ÿ8��һ��
	                strByte = stringBuilder.substring(i, i + 8);
	                by[index] = (byte) Integer.parseInt(strByte, 2);
	                index++;
	            }
	        }
	        return by;
	    }

	    //��ȡ����������
	     static void getCodes(Node node, String code, StringBuilder stringBuilder) {
	    	StringBuilder builder = new StringBuilder(stringBuilder);
	        builder.append(code);
	        if (node != null) {
	            if (node.data == null) {  //�������Ҷ�ӽڵ�
	                getCodes(node.left, "0", builder);
	                getCodes(node.right, "1", builder);
	            } else {
	                huffmanCodes.put(node.data, builder.toString());
	            }
	        }
	    }
	    
	  //����
	    static Map<Byte, String> getCodes(Node root) {
	    	StringBuilder stringBuilder = new StringBuilder();
	        if (root == null) {//��������Ϊ��
	            return null;
	        }
	        getCodes(root.left, "0", stringBuilder);//����ݹ�
	        getCodes(root.right, "1", stringBuilder);//���ҵݹ�
	        return huffmanCodes;
	    }

	    //���ɹ�������
	    static Node createHuffmanTree(List<Node> nodes) {
	        while (nodes.size() > 1) {
	            Collections.sort(nodes);

	            Node leftNode = nodes.get(0);
	            Node rightNode = nodes.get(1);

	            Node parent = new Node(null, leftNode.weight + rightNode.weight);
	            parent.left = leftNode;
	            parent.right = rightNode;

	            nodes.remove(leftNode);
	            nodes.remove(rightNode);
	            nodes.add(parent);
	        }
	        return nodes.get(0);
	    }

	    //�����ֽ�����
	   static List<Node> getNodes(byte[] bytes) {
	        List<Node> nodes = new ArrayList<>();
	        Map<Byte, Integer> counts = new HashMap<>();
	        for (byte b : bytes) {
	            Integer count = counts.get(b);
	            if (count == null) {//�ַ���һ�γ���
	                counts.put(b, 1);
	            } else {//�ַ��ظ�
	                counts.put(b, count + 1);//��Ӧԭ����ֵ��һ
	            }
	        }
	        //����map
	        for (Map.Entry<Byte, Integer> entry : counts.entrySet()) {
	            nodes.add(new Node(entry.getKey(), entry.getValue()));//����ֵ�Լӵ�node������
	        }
	        return nodes;
	    }
	    
	   static int Wpl(Node root) {
			if(root!=null) {
				if(root.left!=null&&root.right!=null) {
					wpl+=root.weight;
				}
				Wpl(root.left);
				Wpl(root.right);
			}
			return wpl;
		}
}


