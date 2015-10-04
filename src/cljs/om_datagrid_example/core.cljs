(ns om-datagrid-example.core
  (:require [om.core :as om :include-macros true]
            [om-tools.dom :as d :include-macros true]
            [om-tools.core :refer-macros [defcomponent]]
            [om-bootstrap.table :refer [table]]
            [om-bootstrap.pagination :as pg]
            [clojure.walk :refer [stringify-keys]]))

(enable-console-print!)

(defonce app-state (atom {:title "Datagrid example!"
                          :data  [{:first "Ben" :last "Bitdiddle" :email "benb@mit.edu"}
                                  {:first "Alyssa" :last "Hacker" :email "aphacker@mit.edu"}
                                  {:first "Eva" :last "Ator" :email "eval@mit.edu"}
                                  {:first "Louis" :last "Reasoner" :email "prolog@mit.edu"}
                                  {:first "Cy" :last "Effect" :email "bugs@mit.edu"}
                                  {:first "Lem" :last "Tweakit" :email "morebugs@mit.edu"}]}))

(defn view-row [contact]
  (map #(d/td nil %) contact))

(defn main []
  (om/root
    (fn [app owner]
      (reify
        om/IRender
        (render [_]
          (d/div nil
                 (d/h1 nil (:title app))
                 (d/div nil
                        (table {:striped? true :bordered? true :condensed? true :hover? true}
                               (d/thead
                                 (d/tr
                                   (map #(d/th nil %) (keys (stringify-keys (first (:data @app-state)))))))
                               (d/tbody
                                 (for [row (:data @app-state)]
                                   (d/tr
                                     (for [col row]
                                       (d/td (last col)))))
                                 ))
                        (pg/pagination {}
                                       (pg/previous {})
                                       (pg/page {} "1")
                                       (pg/page {} "2")
                                       (pg/page {} "3")
                                       (pg/next {})))))))
    app-state
    {:target (. js/document (getElementById "app"))}))
