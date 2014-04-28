package testDrivers;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.Vector;

public class parserEmulator {

	/**
	 * @param args
	 */
	public static void main(String[] args) 
	{
		Random r = new Random();
		Vector<node_base> nodes = new Vector<node_base>();
		node_base root = new tree_node1();
		nodes.add( root );
		
		for ( int i = 0; i < 100; ++i ) {
			node_base v = null;
			node_base p = null;
			
			switch ( r.nextInt() % 3 ) {
			case 0:
				v = new tree_node1();
				break;
				
			case 1:
				v = new tree_node2();
				break;
				
			case 2:
				v = new tree_node3();
				break;
			}
			
			p = nodes.elementAt( r.nextInt() % nodes.size() );
			v.setParent(p);
			p.insertChild(v);
			nodes.add( v );
		}
		
		for ( int i = 0; i < nodes.size(); ++i ) {
			node_base p = nodes.elementAt(i);
			if ( p.visit == false ) dfs( p );
		}
	}

	public static void dfs( node_base p )
	{
		for ( node_base v : p.followers ) {
			if ( v.visit == false ) dfs(v);
		}
		
		p.clean();
	}
}

abstract class node_base
{
	public boolean visit = false;
	public node_base parent = null;
	public Set<node_base> followers = new HashSet<node_base>();
	
	public boolean isRoot()
	{
		return parent == null;
	}

	public void clean()
	{
		this.getParent().removeChild(this);
	}
	
	public abstract void insertChild( node_base v );
	public abstract void removeChild( node_base v );
	public abstract void setParent( node_base v );
	public abstract node_base getParent();
}

class tree_node1 extends node_base
{

	public void insertChild( node_base v )
	{
		followers.add(v);
	}
	
	public void removeChild( node_base v )
	{
		followers.remove(v);
	}
	
	public void setParent( node_base v )
	{
		parent = v;
	}
	
	public node_base getParent()
	{
		return parent;
	}
}

class tree_node2 extends node_base
{
	public void insertChild( node_base v )
	{
		followers.add(v);
	}
	
	public void removeChild( node_base v )
	{
		followers.remove(v);
	}
	
	public void setParent( node_base v )
	{
		parent = v;
	}
	
	public node_base getParent()
	{
		return parent;
	}
}

class tree_node3 extends node_base
{
	public void insertChild( node_base v )
	{
		followers.add(v);
	}
	
	public void removeChild( node_base v )
	{
		followers.remove(v);
	}
	
	public void setParent( node_base v )
	{
		parent = v;
	}
	
	public node_base getParent()
	{
		return parent;
	}
}
