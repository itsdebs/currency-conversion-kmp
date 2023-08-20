//
//  ComposeView.swift
//  CurrencyConversioniOS
//
//  Created by Debanjan Chatterjee on 27/07/23.
//  Copyright © 2023 orgName. All rights reserved.
//

import Foundation
import SwiftUI
import shared

struct ComposeView: UIViewControllerRepresentable{
    func updateUIViewController(_ uiViewController: UIViewControllerType, context: Context) {
       
    }
    func makeUIViewController(context: Context) -> some UIViewController {
        AppKt.MainViewController()
    }
}
