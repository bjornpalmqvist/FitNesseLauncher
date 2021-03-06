package com.processpuzzle.fitnesse.launcher.maven.plugin.fitnesse.mojo;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.apache.commons.io.FileUtils;
import org.apache.maven.plugin.MojoExecution;
import org.codehaus.plexus.util.xml.Xpp3Dom;
import org.codehaus.plexus.util.xml.Xpp3DomBuilder;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import com.processpuzzle.fitnesse.launcher.maven.plugin.fitnesse.mojo.TearDownMojo;

public class TearDownMojoTest extends MojoTest{
   private SetupsMojoTestHelper helper;

   @Before @Override public void beforeEachTest() throws Exception {
      super.beforeEachTest();
      helper = new SetupsMojoTestHelper( new TearDownMojo() );
      helper.setupArtifact( "org.apache.maven.plugins", "maven-clean-plugin", "clean", "maven-plugin" );
   }

   @After @Override public void afterEachTest() throws Exception {
      FileUtils.deleteQuietly( helper.workingDir );
      super.afterEachTest();
   }

   @Test
   public void testExecute() throws Exception {
      final Xpp3Dom cleanConfig = Xpp3DomBuilder.build( TearDownMojoTest.class.getResourceAsStream( "teardown-clean-mojo-config.xml" ), "UTF-8" );

      doAnswer( new Answer<Void>() {
         @Override
         public Void answer( InvocationOnMock invocation ) throws Throwable {
            assertEquals( cleanConfig, ((MojoExecution) invocation.getArguments()[1]).getConfiguration() );
            return null;
         }
      } ).when( helper.mojo.pluginManager ).executeMojo( eq( helper.mojo.session ), any( MojoExecution.class ) );

      helper.mojo.execute();

      verify( helper.mojo.pluginManager, times( 1 ) ).executeMojo( eq( helper.mojo.session ), any( MojoExecution.class ) );
   }
}
