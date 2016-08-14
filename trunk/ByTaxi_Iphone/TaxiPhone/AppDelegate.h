//
//  AppDelegate.h
//  TaxiPhone
//
//  Created by Herivelto Gabriel on 3/15/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#import <UIKit/UIKit.h>

@class RootViewController;
@class LoginViewController;

//@interface AppDelegate : UIResponder <UIApplicationDelegate>
@interface AppDelegate : NSObject <UIApplicationDelegate>{
    
}

@property (strong, nonatomic) UIWindow *window;
@property (strong, nonatomic) UINavigationController *navigationController; 
@property (readonly, strong, nonatomic) NSManagedObjectContext *managedObjectContext;
@property (readonly, strong, nonatomic) NSManagedObjectModel *managedObjectModel;
@property (readonly, strong, nonatomic) NSPersistentStoreCoordinator *persistentStoreCoordinator;

- (void)saveContext;
- (NSURL *)applicationDocumentsDirectory;


@end
