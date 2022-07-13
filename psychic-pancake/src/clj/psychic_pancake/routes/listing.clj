(def listing-routes
  ["/listings/"
   [""
    {:swagger {:tags ["listing"]}
     :get {:handler (constantly (ok))}  ;get listings
     :post {:handler (constantly (ok))}}
    
    ]])
