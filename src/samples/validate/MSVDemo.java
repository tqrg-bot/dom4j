/*
 * Copyright 2001 (C) MetaStuff, Ltd. All Rights Reserved.
 * 
 * This software is open source. 
 * See the bottom of this file for the licence.
 * 
 * $Id$
 */

package validate;

import com.sun.msv.grammar.Grammar;
import com.sun.msv.reader.util.GrammarLoader;
import com.sun.msv.reader.util.IgnoreController;
import com.sun.msv.verifier.DocumentDeclaration;
import com.sun.msv.verifier.ValidityViolation;
import com.sun.msv.verifier.Verifier;
import com.sun.msv.verifier.VerificationErrorHandler;

import javax.xml.parsers.SAXParserFactory;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.SAXReader;
import org.dom4j.io.SAXWriter;

import org.xml.sax.ContentHandler;
import org.xml.sax.ErrorHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXParseException;

/** A sample program which validates an already existing dom4j Document
  * using Sun's MSV library.
  *
  * @author <a href="mailto:jstrachan@apache.org">James Strachan</a>
  * @version $Revision$
  */
public class MSVDemo {
    
    public static void main(String[] args) {
        new MSVDemo().run( args );
    }    
    
    public void run(String[] args) {
        try {
            if ( args.length < 2 ) {
                System.out.println( "usage: <xmlDoc> <schemaDoc>" );
                System.out.println( "Which validates the given XML document against the given schema document" );
                System.out.println( "The schema can be XML Schema, RelaxNG, Relax or TREX" );
                return;
            }
            String xmlFile = args[0];
            String schema = args[1];
            
            SAXReader reader = new SAXReader();
            Document document = reader.read( xmlFile );
            process( document, schema );
        }
        catch (DocumentException e) {
            System.out.println( "Exception occurred: " + e );
            Throwable nestedException = e.getNestedException();
            if ( nestedException != null ) {
                System.out.println( "NestedException: " + nestedException );
                nestedException.printStackTrace();
            }
            else {
                e.printStackTrace();
            }
        }
        catch (Throwable t) {
            System.out.println( "Exception occurred: " + t );
            t.printStackTrace();
        }
    }
    
    /** Validate document using MSV */
    protected void process(Document document, String schema) throws Exception {
        SAXParserFactory saxFactory = SAXParserFactory.newInstance();
        saxFactory.setNamespaceAware( true );
        
        DocumentDeclaration docDeclaration = GrammarLoader.loadVGM(
            schema,
            new IgnoreController() {
                public void error(Locator[] locations, String message, Exception e) {
                    System.out.println( "ERROR: " + message );
                }
                public void warning(Locator[] locations, String message) {
                    System.out.println( "WARNING: " + message );
                }
            },
            saxFactory 
        );
        
        System.out.println( "Loaded schema document: " + docDeclaration );
        
        Verifier verifier = new Verifier( 
            docDeclaration, 
            new VerificationErrorHandler() {
                public void onError(ValidityViolation e) {
                    System.out.println( "Verification ERROR: " + e );
                }
                public void onWarning(ValidityViolation e) {
                    System.out.println( "Verification WARNING: " + e );
                }
            }
        );
        
        System.out.println( "Validating XML document" );
        
        SAXWriter writer = new SAXWriter( (ContentHandler) verifier );
        writer.setErrorHandler(
            new ErrorHandler() {
                public void error(SAXParseException e) {
                    System.out.println( "ERROR: " + e );
                }
                
                public void fatalError(SAXParseException e) {
                    System.out.println( "FATAL: " + e );
                }
                
                public void warning(SAXParseException e) {
                    System.out.println( "WARNING: " + e );
                }
            }
        );
        writer.write( document );
    }
}



/*
 * Redistribution and use of this software and associated documentation
 * ("Software"), with or without modification, are permitted provided
 * that the following conditions are met:
 *
 * 1. Redistributions of source code must retain copyright
 *    statements and notices.  Redistributions must also contain a
 *    copy of this document.
 *
 * 2. Redistributions in binary form must reproduce the
 *    above copyright notice, this list of conditions and the
 *    following disclaimer in the documentation and/or other
 *    materials provided with the distribution.
 *
 * 3. The name "DOM4J" must not be used to endorse or promote
 *    products derived from this Software without prior written
 *    permission of MetaStuff, Ltd.  For written permission,
 *    please contact dom4j-info@metastuff.com.
 *
 * 4. Products derived from this Software may not be called "DOM4J"
 *    nor may "DOM4J" appear in their names without prior written
 *    permission of MetaStuff, Ltd. DOM4J is a registered
 *    trademark of MetaStuff, Ltd.
 *
 * 5. Due credit should be given to the DOM4J Project
 *    (http://dom4j.org/).
 *
 * THIS SOFTWARE IS PROVIDED BY METASTUFF, LTD. AND CONTRIBUTORS
 * ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES, INCLUDING, BUT
 * NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL
 * METASTUFF, LTD. OR ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,
 * STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED
 * OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * Copyright 2001 (C) MetaStuff, Ltd. All Rights Reserved.
 *
 * $Id$
 */
