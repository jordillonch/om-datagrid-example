(ns om-datagrid-example.test-runner
  (:require
   [cljs.test :refer-macros [run-tests]]
   [om-datagrid-example.core-test]))

(enable-console-print!)

(defn runner []
  (if (cljs.test/successful?
       (run-tests
        'om-datagrid-example.core-test))
    0
    1))
