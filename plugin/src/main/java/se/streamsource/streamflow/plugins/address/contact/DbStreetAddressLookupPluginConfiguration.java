/**
 * Copyright 2011 Streamsource AB. All Rights Reserved by Streamsource AB.
 *
 * The content of this file is property of Streamsource AB, org no 556777-9896, with the address
 * Hans Michelsensgatan 9, SE-211 20 Malmö, Sweden. Any unauthorized review, use, disclosure
 * or distribution is prohibited.
 */
package se.streamsource.streamflow.plugins.address.contact;

import org.qi4j.api.common.UseDefaults;
import org.qi4j.api.configuration.ConfigurationComposite;
import org.qi4j.api.property.Property;

public interface DbStreetAddressLookupPluginConfiguration
      extends ConfigurationComposite
{
   
   @UseDefaults
   Property<String> query();
   
   @UseDefaults
   Property<Integer> minkeywordlength();

   @UseDefaults
   Property<Integer> limit();
}
