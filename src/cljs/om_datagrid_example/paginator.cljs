(ns om-datagrid-example.paginator
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require [cljs.core.async :refer [put! chan alts! pub <! >!]]
            [om.core :as om :include-macros true]))

(defn- request-data [owner]
  (let [res (chan)]
    (go
      (>! (om/get-shared owner :req-chan)
          {:op       :get-data
           :start    (om/get-state owner :start)
           :per-page (om/get-state owner :per-page)
           :res      res})
      (om/set-state! owner :data (<! res)))))

(defn- modify-page! [owner fn]
  (let [start (om/get-state owner :start)
        per-page (om/get-state owner :per-page)]
    (om/set-state! owner :start (fn start per-page))))

(defn- set-next-page! [owner]
  (modify-page! owner +))

(defn- set-previous-page! [owner]
  (modify-page! owner -))


(defn page-next [owner]
  (set-next-page! owner)
  (request-data owner))

(defn page-previous [owner]
  (set-previous-page! owner)
  (request-data owner))
