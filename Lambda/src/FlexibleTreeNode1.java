import javax.swing.*;
import javax.swing.tree.*;

import java.util.*;

import turban.utils.*;

import java.util.function.*;
 
 
 /**
   * Class for a more flexible TreeNode than DefaultMutableTreeNode.
   * The class is programmed as a generic working on IGuifiable objects
   */
  public class FlexibleTreeNode1<T extends IGuifiable>  extends DefaultMutableTreeNode implements IDebugable
  {
	  /**
	   * Constructor
	   * IN: The user object to be retrieved by 'getUserObject()'.
	   */
	  public FlexibleTreeNode1(T userObj)
	  {
		  super(userObj);
		  // NOTE (BeTu; 2015-05-19): 'super'-Statement must always be first statement in constructor.
		  // Therefore the check for != null is performed afterwards. This is not so nice, but it's quite ok. 
		  ErrorHandler.Assert(userObj!=null, true, FlexibleTreeNode1.class, "No user object provided!");
	  }
	  
	  
	  @Override
	  public void setUserObject(Object userObj)
	  {
		  throw new UnsupportedOperationException ();
	  }
	  
	  @Override
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
	  private void toDebugString_resursive(StringBuilder sb,int indentLvl,FlexibleTreeNode1<T> tn )
	  {
		 toDebugString_indent( sb, indentLvl);
		 sb.append(tn.toString());
		 sb.append("\n");
		 for (TreeNode tnChild:tn.getChildren())
		 {
			 toDebugString_resursive(sb,indentLvl+1,(FlexibleTreeNode1<T>) tnChild );
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
			  ErrorHandler.logException (th, false,FlexibleTreeNode1.class
			                                  , "Error resolving toGuiString()" );
		      return this.getClass().getName()+": [Unresolvable Symbol!]";
		  }
	  }
	  
	  /**
	   * Returns the children as Iterator
	   */
	  public Iterable<FlexibleTreeNode1<T>> getChildren() {
		  final Enumeration enumChildren=this.children();
		  return new Iterable<FlexibleTreeNode1<T>>() {
			  public Iterator<FlexibleTreeNode1<T>> iterator() 
			  {
				  return new Iterator <FlexibleTreeNode1<T>> () 
				  {
					 public boolean hasNext()
					 {
							return enumChildren.hasMoreElements();
					 }
						
					 @SuppressWarnings("unchecked")
					 public FlexibleTreeNode1<T> next()
					 {
						return (FlexibleTreeNode1<T>)enumChildren.nextElement();
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
								(Predicate<FlexibleTreeNode1<T>> cond, List<FlexibleTreeNode1<T>> lst){
          if(cond.test(this)==true){
			  lst.add(this);
		  }		
		
		  for (int i=0; i< this.getChildCount(); i++)
		  {
			   @SuppressWarnings("unchecked")
			   FlexibleTreeNode1<T> tnChild=(FlexibleTreeNode1<T>) this.getChildAt(i);
			   
			   tnChild.getAllNodesWithCondition_recursive(cond,  lst);
		  }
				
	  }
	  
	  /**
	   * Gets all tree nodes of the tree meeting a condition
	   *
	   * @param cond IN: The condition to test as interface
	   *
	   * @return the treenodes meeting the condition
	   */
	  public List<FlexibleTreeNode1<T>> getAllNodesWithCondition
								(Predicate<FlexibleTreeNode1<T>> cond){
		 List<FlexibleTreeNode1<T>> lst=new ArrayList<FlexibleTreeNode1<T>>();
		 
		 this.getAllNodesWithCondition_recursive(cond, lst);
		 
		 return lst;
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
	  
	  
	  
	  
	  
	  
	  
	  private void getLeaves_recursive(FlexibleTreeNode1<T> tnCurrent, List<FlexibleTreeNode1<T>> lstLeaves)
      {
		  if(tnCurrent.getChildCount()==0)  //if(tnCurrent.isLeaf())
		  {
			  lstLeaves.add(tnCurrent);
		  }  
	  
		  Enumeration enChildren= tnCurrent.children();
		  while(enChildren.hasMoreElements() == true)
		  {
			  @SuppressWarnings("unchecked")
			  FlexibleTreeNode1<T> tnChild=(FlexibleTreeNode1<T>)enChildren.nextElement();
			  
			  getLeaves_recursive(tnChild, lstLeaves);
		  }
		  
		  /* Statt while-Schleife ist auch for-Schleife möglich:
		  for (int i=0;i< tnCurrent.getChildCount();i++)
		  {
			   TreeNode tnChild=tnCurrent.getChildAt(i);
			    getLeaves_recursive((FlexibleTreeNode<T>)tnChild, lstLeaves);
		  }
		  */
		  
	  }

	  /**
	   * Starts with this FlexibleTreeNode to get all leaves of this tree.
	   *
	   * @return List with all TreeNodes being a leaf. 
	   */
      public List<FlexibleTreeNode1<T>> getLeaves()
	  {
	      List<FlexibleTreeNode1<T>> lstLeaves=new ArrayList<FlexibleTreeNode1<T>>() ;
		  
		  getLeaves_recursive(this,lstLeaves);
		  
		  return lstLeaves;
	  }
	  
	  
	  
	  
	  private void getAllTreeNodesAsList_recursive(List<FlexibleTreeNode1<T>> lstTnds
													, FlexibleTreeNode1<T> tnCurrent)
	  {
		  lstTnds.add(tnCurrent);
		  for(int i=0; i< tnCurrent.getChildCount(); i++)
		  {
			    @SuppressWarnings("unchecked")
				FlexibleTreeNode1<T> tnChild=(FlexibleTreeNode1<T>)tnCurrent.getChildAt(i);
				getAllTreeNodesAsList_recursive(lstTnds, tnChild);
		  }
	  }
	  
	  /**
	   * Starts with this FlexibleTreeNode to get all TreeNodes of this tree as a list.
	   *
	   * @return List of all TreeNodes. 
	   */
	  public List<FlexibleTreeNode1<T>> getAllTreeNodesAsList()
	  {
		  List<FlexibleTreeNode1<T>> lstTnds=new ArrayList<FlexibleTreeNode1<T>>();
		  getAllTreeNodesAsList_recursive(lstTnds,this);
		  return lstTnds;
	  }
	  
	 
	  
	   
	  /**
	   * Gets the selected tree nodes of a JTree as a list of FlexibleTreeNode objects.
	   * NOTE: This assumes that the TreeModel of the JTree works on FlexibleTreeNode<R> Objects!
	   *
	   * @param jtree IN: The JTree
	   * @param typeR IN: Class object defining the type R for the FlexibleTreeNode<R> Objects to be retrieved.
	   *
	   * @return List of all selected TreeNodes. 
	   */
	  public static <R extends IGuifiable> List<FlexibleTreeNode1<R>> getSelectedTreeNodes
																		(JTree jtree,Class<R> typeR)
	  {
		  List<FlexibleTreeNode1<R>> lstSelTnds=new ArrayList<FlexibleTreeNode1<R>> ();
		  
		  if(jtree.getSelectionPaths()==null)
		  {
			  return lstSelTnds;
		  }
		  
		  for(TreePath treePath: jtree.getSelectionPaths())
		  {
				try
				{
					@SuppressWarnings("unchecked")
					FlexibleTreeNode1 <R> tnSelected
						 =(FlexibleTreeNode1 <R>)treePath.getLastPathComponent();
					lstSelTnds.add(tnSelected);
				}
				catch(Throwable ex)
				{
					//NOTE: Make safety-line because the cast to FlexibleTreeNode <R>
					ErrorHandler.logException (ex, false,FlexibleTreeNode1.class
					  , "Error casting last path component of TreePath [{0}] to FlexibleTreeNode<{1}>."
					  ,treePath,typeR);
				}
		  }
		  return lstSelTnds;
	  }
	  
	   /**
	   * Gets the selected user objects of a JTree as a list.
	   * NOTE: This assumes that the TreeModel of the JTree works on FlexibleTreeNode<R> Objects!
	   *
	   * @param jtree IN: The JTree
	   * @param typeR IN: Class object defining the type R for the Objects to be retrieved.
	   *
	   * @return List of all selected user objects. 
	   */
	  public static <R extends IGuifiable> List<R> getSelectedUserObjects(JTree jtree,Class<R> typeR)
	  {
		  List<R> lstSelUserObjs=new ArrayList<R> ();
		  
	      List<FlexibleTreeNode1<R>> lstSelTnds=FlexibleTreeNode1.getSelectedTreeNodes(jtree, typeR);
	      for(FlexibleTreeNode1<R> tnSel:lstSelTnds)
		  {
			  lstSelUserObjs.add(tnSel.getUserObject());
		  }			  
	  
	      return lstSelUserObjs;
	  }
	  
	  
  }