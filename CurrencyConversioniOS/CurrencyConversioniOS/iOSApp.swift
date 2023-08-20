import SwiftUI
import BackgroundTasks
import shared
@main
struct iOSApp: App {
    
    init(){
        let arr = Array<Koin_coreModule>()
        let kotlinEmptyArray = KotlinArray<Koin_coreModule>(size: 0) { kotlinInt in
            arr[kotlinInt.intValue]
        }
        HelpersKt.doInitKoin(customInitialization: nil, extraModules: kotlinEmptyArray)
        registerBackgroundTasks()
    }
    func registerBackgroundTasks(){
        let backgroundAppRefreshTaskSchedulerIdentifier = "com.debanjan.payapycoding-workmanager.recurringbackgroundtask"
        
        // Use the identifier which represents your needs
        BGTaskScheduler.shared.register(forTaskWithIdentifier: backgroundAppRefreshTaskSchedulerIdentifier, using: nil) { (task) in
            print("BackgroundAppRefreshTaskScheduler is executed NOW!")
            print("Background time remaining: \(UIApplication.shared.backgroundTimeRemaining)s")
            submitBackgroundTasks()
            task.expirationHandler = {
                task.setTaskCompleted(success: false)
            }
            Task{
                let fetchSuccess = await pullExchangeRates()
                DispatchQueue.main.async {
                    task.setTaskCompleted(success: fetchSuccess)
                }
                
            }
            let isFetchingSuccess = true
            task.setTaskCompleted(success: isFetchingSuccess)
        }
    }
    func pullExchangeRates()async -> Bool{
        await withCheckedContinuation { continuation in
            HelpersKt.exchangeRepository.updateExchangeRates { (successK : KotlinBoolean?, error : Error?) in
                if let err = error {
                    continuation.resume(with: Result.success(false))
                }
                if let success : Bool = successK?.boolValue{
                    continuation.resume(with: Result.success(success))
                }
                if(successK == nil && error == nil){
                    continuation.resume(with: Result.success(false))
                }
            }
        }
        
    }
    func submitBackgroundTasks() {
        // Declared at the "Permitted background task scheduler identifiers" in info.plist
        let backgroundAppRefreshTaskSchedulerIdentifier = "com.debanjan.payapycoding-workmanager.recurringbackgroundtask"
        let timeDelay = 30.0 * 60 // 30 minutes

        do {
          let backgroundAppRefreshTaskRequest = BGAppRefreshTaskRequest(identifier: backgroundAppRefreshTaskSchedulerIdentifier)
          backgroundAppRefreshTaskRequest.earliestBeginDate = Date(timeIntervalSinceNow: timeDelay)
          try BGTaskScheduler.shared.submit(backgroundAppRefreshTaskRequest)
          print("Submitted task request")
        } catch {
          print("Failed to submit BGTask")
        }
      }
    var body: some Scene {
        WindowGroup {
            ContentView()
        }
    }
}
