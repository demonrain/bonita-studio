/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.w3._1999.xhtml.validation;

import org.w3._1999.xhtml.BodyType;
import org.w3._1999.xhtml.DirType;
import org.w3._1999.xhtml.HeadType;

/**
 * A sample validator interface for {@link org.w3._1999.xhtml.HtmlType}.
 * This doesn't really do anything, and it's not a real EMF artifact.
 * It was generated by the org.eclipse.emf.examples.generator.validator plug-in to illustrate how EMF's code generator can be extended.
 * This can be disabled with -vmargs -Dorg.eclipse.emf.examples.generator.validator=false.
 */
public interface HtmlTypeValidator {
	boolean validate();

	boolean validateHead(HeadType value);
	boolean validateBody(BodyType value);
	boolean validateDir(DirType value);
	boolean validateId(String value);
	boolean validateLang(String value);
	boolean validateLang1(String value);
}
