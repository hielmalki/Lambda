import javax.swing.*;
import javax.swing.tree.*;

import java.util.*;

import turban.utils.*;

import java.util.function.*;
 
 
 /**
   * Class for a more flexible TreeNode than DefaultMutableTreeNode.
   * The class is programmed as a generic working on IGuifiable objects
   */
  public class FlexibleTreeNode<T extends IGuifiable>  extends DefaultMutableTreeNode implements IDebugable
  {
	  /**
	   * Constructor
	   * IN: The user object to be retrieved by 'getUserObject()'.
	   */
	  public FlexibleTreeNode(T userObj)
	  {
		  super(userObj);
		  // NOTE (BeTu; 2015-05-19): 'super'-Statement must always be first statement in constructor.
		  // Therefore the check for != null is performed afterwards. This is not so nice, but it's quite ok. 
		  ErrorHandler.Assert(userObj!=null, true, FlexibleTreeNode.class, "No user object provided!");
	  }
	  
	  
	  @Override
	  public void setUserObject(Object userObj)
	  {
		  throw new UnsupportedOperationException ();
	  }
	  
	  @SuppressWarnings("unchecked")
	  public T getUserObject()
	  {
		  return (T) super.getUserObject();
	  }
	  
	  /**
	   * Performs indentation (Einrückung) according to the level in the tree.
	   */
	  private void toDebugString_indent(StringBuilder sb,int indentLvl)
	  {
		  for (int i=0; i<indentLvl; i++)
		  {
			  sb.append("  ");
		  }
		  if(indentLvl>0)
		  {
			  sb.append("|-");
		  }
	  }
	  
	  @SuppressWarnings("unchecked")
	  private void toDebugString_resursive(StringBuilder sb,int indentLvl,FlexibleTreeNode<T> tn )
	  {
		 toDebugString_indent( sb, indentLvl);
		 sb.append(tn.toString());
		 sb.append("\n");
		 for (TreeNode tnChild:tn.getChildren())
		 {
			 toDebugString_resursive(sb,indentLvl+1,(FlexibleTreeNode<T>) tnChild );
		 }
	  }
	  
	  /**
	   * String to be resolved for Debugging-Purposes
	   * @return Returns the content of the tree for debugging purposes.
	   */
	  public String toDebugString()
	  {
		  try
		  {
			  StringBuilder sb=new StringBuilder();
			  toDebugString_resursive(sb,0,this );
			  return sb.toString();
		  }
		  catch(Throwable th)
		  {
		       return "Unable to fully resolve " +this.getClass().getName()+" ["+this.toString()+"]";
		  }
	  }
	  
	  
	  /**
	   * Overwritten toString(). The method now calls toGuiString of the generic object to do easier working
	   * with the IGuifiable-Pattern. As this method is used by JTree.
	   *
	   * @return the string to be displayed in the GUI (e.g., in JTree)
	   */
	  @Override
	  public String toString()
	  {
		  try
		  {
			  return ((IGuifiable)this.getUserObject()).toGuiString();
		  }
		  catch(Throwable th)
		  {
			  ErrorHandler.logException (th, false,FlexibleTreeNode.class
			                                  , "Error resolving toGuiString()" );
		      return this.getClass().getName()+": [Unresolvable Symbol!]";
		  }
	  }
	  
	  /**
	   * Returns the children as Iterator
	   */
	  public Iterable<FlexibleTreeNode<T>> getChildren() {
		  final Enumeration enumChildren=this.children();
		  return new Iterable<FlexibleTreeNode<T>>() {
			  public Iterator<FlexibleTreeNode<T>> iterator() 
			  {
				  return new Iterator <FlexibleTreeNode<T>> () 
				  {
					 public boolean hasNext()
					 {
							return enumChildren.hasMoreElements();
					 }
						
					 @SuppressWarnings("unchecked")
					 public FlexibleTreeNode<T> next()
					 {
						return (FlexibleTreeNode<T>)enumChildren.nextElement();
					 }
						
					public void remove()
					{
						throw new UnsupportedOperationException ();
					}
				  };
			  }
		  };
	  }
	  
	   /**
	   * Counts the tree nodes underneath the start node including the start node
	   * @param tnStart IN: The tree node to start
	   * @return the number of nodes
	   */
	  public int countTreeNodes(TreeNode tnStart)
	  {
		  if(tnStart==null)
		  {
			  return 0;
		  }
		  
		  int iCount=1; // 0+1 (==this node)
		  for(int i=0; i< tnStart.getChildCount(); i++)
		  {
			 TreeNode tnChild=tnStart.getChildAt(i);
			 iCount+=countTreeNodes(tnChild);
		  }
		  return iCount;
	  }
	  
	  
	  /******************************************The following functions are new in Version2:***************/
	  
	    private void getAllNodesWithCondition_recursive
								(Predicate<FlexibleTreeNode<T>> cond, List<FlexibleTreeNode<T>> lst){
          if(cond.test(this)==true){
			  lst.add(this);
		  }		
		
		  for (int i=0; i< this.getChildCount(); i++)
		  {
			   @SuppressWarnings("unchecked")
			   FlexibleTreeNode<T> tnChild=(FlexibleTreeNode<T>) this.getChildAt(i);
			   
			   tnChild.getAllNodesWithCondition_recursive(cond,  lst);
		  }
				
	  }
	    public List<FlexibleTreeNode<T>> getAllTreeNodesAsList() {
			List<FlexibleTreeNode<T>> lstTnds = new ArrayList<FlexibleTreeNode<T>>();
			getAllTreeNodesAsList_recursive(lstTnds, this);
			return lstTnds;
		}
	    private void getAllTreeNodesAsList_recursive(List<FlexibleTreeNode<T>> lstTnds, FlexibleTreeNode<T> tnCurrent) {
			lstTnds.add(tnCurrent);
			for (int i = 0; i < tnCurrent.getChildCount(); i++) {
				@SuppressWarnings("unchecked")
				FlexibleTreeNode<T> tnChild = (FlexibleTreeNode<T>) tnCurrent.getChildAt(i);
				getAllTreeNodesAsList_recursive(lstTnds, tnChild);
			}
		}
	  
	  /**
	   * Gets all tree nodes of the tree meeting a condition
	   *
	   * @param cond IN: The condition to test as interface
	   *
	   * @return the treenodes meeting the condition
	   */
	  public List<FlexibleTreeNode<T>> getAllNodesWithCondition
								(Predicate<FlexibleTreeNode<T>> cond){
		 List<FlexibleTreeNode<T>> lst=new ArrayList<FlexibleTreeNode<T>>();
		 
		 this.getAllNodesWithCondition_recursive(cond, lst);
		 
		 return lst;
	  }
	  //Rekursive Lösung
	  public void forEachAsLambda(BiConsumer<FlexibleTreeNode<T>, T> biConsumer) {
			biConsumer.accept(this,this.getUserObject());
			
			this.getChildren().forEach(x->x.forEachAsLambda(biConsumer));
			

		}
	  
	  
	  public void forEach(BiConsumer<FlexibleTreeNode<T>, T> biConsumer) {
			if(!this.isLeaf())
				for (FlexibleTreeNode<T> node : this.getChildren()) 
					node.forEach(biConsumer);
			if(this.getUserObject() != null)
				biConsumer.accept(this,this.getUserObject());
		}

		public void forEachForNotRekursiv(BiConsumer<FlexibleTreeNode<T>, T> biConsumer) {
			for (FlexibleTreeNode<T> node : getAllTreeNodesAsList()) {
				biConsumer.accept((FlexibleTreeNode<T>) node, (T) node.getUserObject());
			}
		}
	  
	 
  }


       /* Beispiel Verwendung von 	getAllNodesWithCondition:							
		  FlexibleTreeNode<MyGuifObj> tn=...;
		  ...
		  List<FlexibleTreeNode<MyGuifObj>> lst= tn.getAllNodesWithCondition(
			new Predicate<FlexibleTreeNode<MyGuifObj>>(){
				  public boolean test (FlexibleTreeNode<MyGuifObj> tn){
					  ...
				  }
			} );
		  */
	  
	  
	  
	  