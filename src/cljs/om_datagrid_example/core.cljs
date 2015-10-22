(ns om-datagrid-example.core
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require [cljs.core.async :refer [put! chan alts! pub <! >!]]
            [om.core :as om :include-macros true]
            [om-tools.dom :as d :include-macros true]
            [om-tools.core :refer-macros [defcomponent]]
            [om-bootstrap.table :refer [table]]
            [om-bootstrap.pagination :as pg]
            [clojure.walk :refer [stringify-keys]]
            [om-datagrid-example.paginator :as paginator]
            [om-datagrid-example.data-bridge :as data-bridge]))

(enable-console-print!)

(defn get-headers [data]
  (keys (stringify-keys (first data))))

(defn contact-row-view [contact owner]
  (reify
    om/IRender
    (render [_]
      (d/tr
        (for [col contact]
          (d/td (last col)))))))

(defn contacts-list-view [app owner]
  (reify
    om/IInitState
    (init-state [_]
      {:start 0 :per-page 3 :data nil})
    om/IDidMount
    (did-mount [_]
      (let [res (chan)]
        (go
          (>! (om/get-shared owner :req-chan)
              {:op       :get-data
               :start    (om/get-state owner :start)
               :per-page (om/get-state owner :per-page)
               :res      res})
          (om/set-state! owner :data (<! res)))))
    om/IRenderState
    (render-state [_ {:keys [data]}]
      (if data
        (d/div nil
               (d/h1 nil "Datagrid example!")
               (d/div nil
                      (table {:striped? true :bordered? true :condensed? true :hover? true}
                             (d/thead
                               (apply d/tr nil
                                      (map #(d/th nil %) (get-headers data))))
                             (d/tbody
                               (om/build-all contact-row-view data)))
                      (pg/pagination {}
                                     ; @todo
                                     (pg/previous {:on-click #(paginator/page-previous owner)})
                                     (pg/page {:active? true} "1")
                                     (pg/page {} "2")
                                     (pg/page {} "3")
                                     (pg/next {:on-click #(paginator/page-next owner)}))))
        (d/div "Loading ...")))))


(defn main []
  (let [req-chan (chan)
        pub-chan (chan)
        notif-chan (pub pub-chan :topic)]

    ;; server loop
    (go
      (while true
        (data-bridge/serve (<! req-chan))))

    (om/root
      contacts-list-view
      nil
      {:shared {:req-chan   req-chan
                :notif-chan notif-chan
                :pub-chan   pub-chan}
       :target (. js/document (getElementById "app"))})))
