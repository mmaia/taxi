//
//  Alerts.m
//  DataBase
//
//  Created by herivelto on 9/3/10.
//  Copyright 2010 __MyCompanyName__. All rights reserved.
//

#import "Alerts.h"


@implementation Alerts

//=======================================================================================================================================
/* 
 Alert with 2 parameter
 
   @Title
   @message 
*/

-(void) alert:  title : messagem {
	
	UIAlertView *alert=[[UIAlertView alloc] initWithTitle:title
												  message: messagem 
												 delegate:self 
										cancelButtonTitle:@"OK" 
										otherButtonTitles:nil, nil];
	[alert show];
				
}	

//=======================================================================================================================================
/* 
 Alert with 3 parameter
 
   @Title
   @message
   @button
*/

-(void) alert:title :messagem :button{
	
	UIAlertView *alert=[[UIAlertView alloc] initWithTitle:title
												  message: messagem 
												 delegate:self 
										cancelButtonTitle:button 
										otherButtonTitles:nil,nil, nil];
	[alert show];
}	


//=======================================================================================================================================
/* 
 Alert with 4 parameter
 
 @Title
 @message
 @button
 @outherButton1
*/
-(void) alert:title :messagem :button :otherButton1 {
	
	UIAlertView *alert=[[UIAlertView alloc] initWithTitle:title
												  message: messagem 
												 delegate:self 
										cancelButtonTitle:button 
										otherButtonTitles:otherButton1,nil];
	[alert show];
}

//=======================================================================================================================================
/* 
 Alert with 5 parameter
 
 @Title
 @message
 @button
 @outherButton1
 @outherButton2
 */
-(void) alert:title :messagem :button :otherButton1 :otherButton2  {
	
	UIAlertView *alert=[[UIAlertView alloc] initWithTitle:title
												  message: messagem 
												 delegate:self 
										cancelButtonTitle:button 
										otherButtonTitles:otherButton1,otherButton2,nil];
	[alert show];
}

//=======================================================================================================================================
@end
