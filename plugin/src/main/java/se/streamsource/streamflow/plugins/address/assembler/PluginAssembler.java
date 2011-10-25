/**
 * Copyright 2011 Streamsource AB. All Rights Reserved by Streamsource AB.
 *
 * The content of this file is property of Streamsource AB, org no 556777-9896, with the address
 * Hans Michelsensgatan 9, SE-211 20 Malmö, Sweden. Any unauthorized review, use, disclosure
 * or distribution is prohibited.
 */
package se.streamsource.streamflow.plugins.address.assembler;

import javax.sql.DataSource;

import org.qi4j.api.common.Visibility;
import org.qi4j.bootstrap.Assembler;
import org.qi4j.bootstrap.AssemblyException;
import org.qi4j.bootstrap.ModuleAssembly;
import org.qi4j.spi.service.importer.ServiceInstanceImporter;

import se.streamsource.infrastructure.database.DataSourceConfiguration;
import se.streamsource.infrastructure.database.DataSourceService;
import se.streamsource.streamflow.plugins.address.contact.DbSteetAddressLookupPlugin;
import se.streamsource.streamflow.plugins.address.contact.DbStreetAddressLookupPluginConfiguration;

/**
 * Register reference plugins in the plugin application
 */
public class PluginAssembler implements Assembler
{
   public void assemble(ModuleAssembly module) throws AssemblyException
   {
      module.entities( DataSourceConfiguration.class ).visibleIn( Visibility.application );
      module.forMixin( DataSourceConfiguration.class ).declareDefaults().properties().set("");
      
      module.services( DataSourceService.class ).identifiedBy( "datasource" ).visibleIn( Visibility.application );
      module.importedServices( DataSource.class ).importedBy( ServiceInstanceImporter.class )
            .setMetaInfo( "datasource" ).identifiedBy( "streetds" ).visibleIn( Visibility.application );

      module.entities( DbStreetAddressLookupPluginConfiguration.class ).visibleIn( Visibility.application );

      module.services( DbSteetAddressLookupPlugin.class ).identifiedBy( "databasestreetaddresslookup" )
            .visibleIn( Visibility.application ).instantiateOnStartup();

   }
}
