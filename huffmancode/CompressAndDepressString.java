package huffmancode;
//�û���������ѹ�����ѹ
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import javax.sql.rowset.JoinRowSet;

public class CompressAndDepressString {
	
	static Map<Byte, String> huffmanCodes = new HashMap<Byte, String>();//�����������
	static int endLen;//��¼���һ���ֽڵĶ����ƴ��ĳ���
	static Node huffmantree;//���������ĸ����
	static int wpl;//���Ĵ�Ȩ·������
	
	public static void main(String[] args) {
		// TODO �Զ����ɵķ������
		String r = new String();
		System.out.println("������һ���ַ�����");
		Scanner reader = new Scanner(System.in);
		r = reader.nextLine();
		byte [] b = r.getBytes();
		byte [] newbyte = huffmanZip(b);
		System.out.println("ѹ����Ϊ��");
		System.out.println((double)newbyte.length/b.length);
		
		System.out.println("�����������Ϊ��");
        for (Map.Entry<Byte,String > entry : huffmanCodes.entrySet()) {
        	Byte a =entry.getKey();
        	byte c = a.byteValue();
			System.out.println(c+":"+entry.getValue());
		}
		System.out.println("����������Ϊ��");
		int count = 0;
		for (byte a : b) {
			System.out.print(huffmanCodes.get(a));
			count++;
			while(count>100) {
				System.out.println();
				count = 0;//ʹ��ӡ�����ı�����ӵ�����
				}
			}
		System.out.println();
	   System.out.println("WplΪ��");
       System.out.println(Wpl(huffmantree));
	   System.out.println("ԭ�����ֽ����飺");
	   for (byte c : b) {
		System.out.print(c);
	   }
	   System.out.println();
	   byte  bytes [] = huffmanUnzip(huffmanCodes, newbyte);
	   System.out.println("��ѹ����ֽ�����Ϊ��");
	   for (int i = 0; i < bytes.length; i++) {
		   System.out.print(bytes[i]);
	   }
	   System.out.println();
	   System.out.println("��ѹ����ַ���Ϊ��");
	   System.out.println(new String(bytes));
   
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
       if(root!=null) {
    	     getCodes(root.left, "0", stringBuilder);//����ݹ�
    	     getCodes(root.right, "1", stringBuilder);//���ҵݹ�
       }
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
  
  //��������ѹ
  static byte[] huffmanUnzip(Map<Byte, String> huffmanCodes, byte[] huffmanBytes) {
      StringBuilder stringBuilder = new StringBuilder();
      for (int i = 0; i < huffmanBytes.length; i++) {
          byte b = huffmanBytes[i];
          boolean flag = (i == huffmanBytes.length - 1);//����Ƿ񵽱�������һλ
          stringBuilder.append(byteToBitString(!flag, b));
      }

      //����,��������,����ֵ�Է�����
      HashMap<String, Byte> map = new HashMap<>();
      for (Map.Entry<Byte, String> entry : huffmanCodes.entrySet()) {
          map.put(entry.getValue(), entry.getKey());
      }

      //���ݱ���ɨ�赽��Ӧ��ASCLL���Ӧ���ַ�
      List<Byte> list = new ArrayList<>();
      for (int i = 0; i < stringBuilder.length();) {
          int count = 1;
          boolean flag = true;
          Byte b = null;
          while (flag) {
        	 String key = stringBuilder.substring(i,i+count);
             b = map.get(key);
             if (b == null) {//�����hash�����Ҳ�����Ӧ�ı���,�����Ӵ��ĳ���
                  count++;
             } else {//�ҵ���
                  flag = false;   
             }
          }
         list.add(b);//�������Ӧ���ֽ���list
         i += count;
      }

      byte b[] = new byte[list.size()];//��list�е��ֽ�ת��Ϊ�ֽ�����
      for (int i = 0; i < b.length; i++) {
          b[i] = list.get(i);
      }
      return b;

  }

  //ת��������
  static String byteToBitString(boolean flag, byte b) {
	int temp = b;//��bת��Ϊint
	temp|=256;
	 String str = Integer.toBinaryString(temp);// ���ص���temp��Ӧ�Ķ����Ʋ���
     if (flag || (flag == false && endLen == 0)) {
   //�ַ����Ľ�ȡ��ֻ�ú��λ
         return str.substring(str.length() - 8);
     } else {
   //����8bit�ж���λ�ö���λ
         return str.substring(str.length() - endLen);
     }
   
  }

}
