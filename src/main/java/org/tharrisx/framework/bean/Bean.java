package org.tharrisx.framework.bean;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.tharrisx.framework.pipe.core.PipeableBean;

/**
 * Each Bean should extend from this base class, or one of the non-final subclasses.
 * 
 * Beans are intimately connected to the PipeManager and to the BeanStore and REST framework, through the StorableBean
 * subclass.
 * 
 * <table border="2" cellpadding="3">
 *   <tr>
 *     <th bgcolor="gray" colspan="2" align="left"><font size="+1" color="white">Annotations for Beans</font></th>
 *   </tr><tr>
 *     <th bgcolor="gray" colspan="2" align="left"><font color="white">XStream Annotations</font></th>
 *   </tr><tr>
 *     <td valign="top" nowrap="nowrap">@XStreamAlias("node-name")</td>
 *     <td valign="top">Sets the node name for a bean type or a bean property. If this is a bean class alias, it must be a unique
 *         node-name within the entire application, as the PipeManager will be keyed off of this to reconstitute
 *         the bean. If this is a bean property alias, it must be a unique node-name within this bean.</td>
 *   </tr><tr>
 *     <td valign="top" nowrap="nowrap">@XStreamAsAttribute</td>
 *     <td valign="top">Defines a bean property to be an attribute on the bean node. NOTE: This is not entirely compatible with
 *         JSON, and we may need to avoid it.</td>
 *   </tr><tr>
 *     <td valign="top" nowrap="nowrap">@XStreamConverter(ConverterImpl.class)</td>
 *     <td valign="top">Defines a type converter for the bean property value. This allows custom formats to be registered
 *     with XStream.
 *     </td>
 *   </tr><tr>
 *     <td valign="top" nowrap="nowrap">@XStreamImplicit</td>
 *     <td valign="top">Marks a collection bean property as 'implicit', ie. it will appear inline to the containing bean as a
 *         repeating node with no wrapper.</td>
 *   </tr><tr>
 *     <td valign="top" nowrap="nowrap">@XStreamOmitField</td>
 *     <td valign="top">Skip this bean property in all cases.</td>
 *   </tr>
 * 
 *   <tr>
 *     <th bgcolor="gray" colspan="2" align="left"><font color="white">REST framework Annotations</font></th>
 *   </tr><tr>
 *     <td valign="top" nowrap="nowrap">@PipePropertyOrder(14.0)</td>
 *     <td valign="top">Define the access type for the bean property. The string name must be used due to our use of
 *     the runtime-mutable enum pattern.</td>
 *   </tr><tr>
 *     <td valign="top" nowrap="nowrap">@PipeProtection(ProtectionType.NAME_FOO)</td>
 *     <td valign="top">Define the protection type for the bean property. The string name must be used due to our
 *     use of a runtime-mutable enum pattern in the ProtectionType class.</td>
 *   </tr><tr>
 *     <td valign="top" nowrap="nowrap">@PipeEncryption(CodecImpl.class)</td>
 *     <td valign="top">Define an encryption algorithm for the bean property as the Class reference.</td>
 *   </tr>
 * 
 *   <tr>
 *     <th bgcolor="gray" colspan="2" align="left"><font size="+1" color="white">Annotations for Resources</font></th>
 *   </tr>
 *   
 *   <tr>
 *     <th bgcolor="gray" colspan="2" align="left"><font color="white">JAX-RS Annotations</font></th>
 *   </tr><tr>
 *     <td valign="top" nowrap="nowrap">@Path("widgets")</td>
 *     <td valign="top">Define a method as a REST resource path named by the argument. Reguler expressions can be used
 *         to provide parameters from the path string. Se the JAX-RS documentation for more details.</td>
 *   </tr>
 * </table>
 * 
 * @author tharrisx
 * @since 1.0.0
 * @version 1.0.0
 */
public abstract class Bean extends PipeableBean {

  public Bean() {
    super();
    // force public default constructor
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this)
        .appendSuper(super.toString())
        .toString();
  }
}
