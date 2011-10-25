/**
 * Copyright 2011 Streamsource AB. All Rights Reserved by Streamsource AB.
 *
 * The content of this file is property of Streamsource AB, org no 556777-9896, with the address
 * Hans Michelsensgatan 9, SE-211 20 Malmö, Sweden. Any unauthorized review, use, disclosure
 * or distribution is prohibited.
 */
package se.streamsource.streamflow.plugins.address.contact;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.qi4j.api.configuration.Configuration;
import org.qi4j.api.injection.scope.Service;
import org.qi4j.api.injection.scope.Structure;
import org.qi4j.api.injection.scope.This;
import org.qi4j.api.mixin.Mixins;
import org.qi4j.api.service.Activatable;
import org.qi4j.api.service.ServiceComposite;
import org.qi4j.api.service.ServiceReference;
import org.qi4j.api.structure.Module;
import org.qi4j.api.value.ValueBuilder;

import se.streamsource.infrastructure.database.Databases;
import se.streamsource.infrastructure.database.Databases.ResultSetVisitor;
import se.streamsource.infrastructure.database.Databases.StatementVisitor;
import se.streamsource.streamflow.server.plugin.address.StreetAddressLookup;
import se.streamsource.streamflow.server.plugin.address.StreetAddressLookupException;
import se.streamsource.streamflow.server.plugin.address.StreetList;
import se.streamsource.streamflow.server.plugin.address.StreetValue;


@Mixins(DbSteetAddressLookupPlugin.Mixin.class)
public interface DbSteetAddressLookupPlugin
      extends ServiceComposite, StreetAddressLookup, Activatable, Configuration
{

   abstract class Mixin implements DbSteetAddressLookupPlugin
   {

      @This
      Configuration<DbStreetAddressLookupPluginConfiguration> config;

      @Service
      ServiceReference<DataSource> dataSource;
      
      @Structure
      Module module;

      public void activate() throws Exception
      {
         config.configuration();
      }
      
      public StreetList lookup(final StreetValue streetTemplate)
      {
         Databases databases = getDatabases();
         
         final ValueBuilder<StreetList> streetsBuilder = module.valueBuilderFactory().newValueBuilder( StreetList.class );
         try
         {
            if (config.configuration().minkeywordlength().get() <= streetTemplate.address().get().length())
            {
               databases.query( config.configuration().query().get(), new StatementVisitor()
               {

                  public void visit(PreparedStatement preparedStatement) throws SQLException
                  {
                     preparedStatement.setString( 1, streetTemplate.address().get() );

                  }
               }, new ResultSetVisitor()
               {

                  public boolean visit(ResultSet visited) throws SQLException
                  {

                     ValueBuilder<StreetValue> streetBuilder = module.valueBuilderFactory().newValueBuilder(
                           StreetValue.class );
                     streetBuilder.prototype().address().set( visited.getString( 1 ) );
                     streetBuilder.prototype().area().set( visited.getString( 2 ) );
                     streetsBuilder.prototype().streets().get().add( streetBuilder.newInstance() );
                     return visited.getRow() <= config.configuration().limit().get();
                  }
               } );
            }
         } catch (SQLException e)
         {
            throw new StreetAddressLookupException( "Could not query database", e );
         }
         return streetsBuilder.newInstance();
      }

      public Databases getDatabases() throws StreetAddressLookupException
      {
         try
         {
            return new Databases(dataSource.get());
         } catch (Exception e)
         {
            throw new StreetAddressLookupException("DataSource not available", e);
         }
      }
   }
}
