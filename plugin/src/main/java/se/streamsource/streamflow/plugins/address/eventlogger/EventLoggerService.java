/**
 * Copyright 2011 Streamsource AB. All Rights Reserved by Streamsource AB.
 *
 * The content of this file is property of Streamsource AB, org no 556777-9896, with the address
 * Hans Michelsensgatan 9, SE-211 20 Malmö, Sweden. Any unauthorized review, use, disclosure
 * or distribution is prohibited.
 */
package se.streamsource.streamflow.plugins.address.eventlogger;

import org.slf4j.LoggerFactory;

import se.streamsource.streamflow.infrastructure.event.domain.DomainEvent;
import se.streamsource.streamflow.infrastructure.event.domain.TransactionDomainEvents;
import se.streamsource.streamflow.infrastructure.event.domain.source.EventStream;
import se.streamsource.streamflow.infrastructure.event.domain.source.TransactionVisitor;

/**
 * JAVADOC
 */
public class EventLoggerService
   implements TransactionVisitor
{
   public EventStream stream;

   public boolean visit( TransactionDomainEvents transactionDomain )
   {
      for (DomainEvent domainEvent : transactionDomain.events().get())
      {
         LoggerFactory.getLogger( "events" ).info( domainEvent.name().get() );
      }

      return true;
   }
}
