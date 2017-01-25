/*
 * JBoss, Home of Professional Open Source
 * Copyright 2009 Red Hat Inc. and/or its affiliates and other contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.arquillian.reporter.api.builder;

import java.security.AccessController;
import java.security.PrivilegedAction;

/**
 * A set of privileged actions that are not to leak out
 * of this package 
 *
 * @version $Revision: $
 */
final class SecurityActions
{

   //-------------------------------------------------------------------------------||
   // Constructor ------------------------------------------------------------------||
   //-------------------------------------------------------------------------------||

   /**
    * No instantiation
    */
   private SecurityActions()
   {
      throw new UnsupportedOperationException("No instantiation");
   }

   //-------------------------------------------------------------------------------||
   // Utility Methods --------------------------------------------------------------||
   //-------------------------------------------------------------------------------||

   /**
    * Obtains the Thread Context ClassLoader
    */
   static ClassLoader getThreadContextClassLoader()
   {
      return AccessController.doPrivileged(GetTcclAction.INSTANCE);
   }

   static boolean isClassPresent(String name) 
   {
      try 
      {
         loadClass(name);
         return true;
      }
      catch (Exception e) 
      {
         return false;
      }
   }

   static Class<?> loadClass(String className)
   {
      try
      {
         return Class.forName(className, true, getThreadContextClassLoader());
      }
      catch (ClassNotFoundException e) 
      {
         try 
         {
            return Class.forName(className, true, SecurityActions.class.getClassLoader());
         }
         catch (ClassNotFoundException e2) 
         {
            throw new RuntimeException("Could not load class " + className, e2);
         }
      }
   }

   //-------------------------------------------------------------------------------||
   // Inner Classes ----------------------------------------------------------------||
   //-------------------------------------------------------------------------------||

   /**
    * Single instance to get the TCCL
    */
   private enum GetTcclAction implements PrivilegedAction<ClassLoader> {
      INSTANCE;

      public ClassLoader run()
      {
         return Thread.currentThread().getContextClassLoader();
      }

   }
}