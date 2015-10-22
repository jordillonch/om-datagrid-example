(ns om-datagrid-example.data-bridge
  (:require [cljs.core.async :refer [put!]]))

; @todo
(def remote-data [{:first "Ben" :last "Bitdiddle" :email "benb@example.com"}
                  {:first "Alyssa" :last "Hacker" :email "aphacker@example.com"}
                  {:first "Eva" :last "Ator" :email "eval@example.com"}
                  {:first "Louis" :last "Reasoner" :email "prolog@example.com"}
                  {:first "Cy" :last "Effect" :email "bugs@example.com"}
                  {:first "Lem" :last "Tweakit" :email "morebugs@example.com"}
                  {:first "John" :last "Wein" :email "wein@example.com"}
                  {:first "Laura" :last "Mit" :email "aphacker@example.com"}
                  {:first "Vicky" :last "Gold" :email "gold@example.com"}
                  {:first "Louis" :last "Amstrong" :email "prolog@example.com"}
                  {:first "Carol" :last "Corner" :email "corner@example.com"}
                  {:first "Jason" :last "Smith" :email "smith@example.com"}])

(defmulti serve :op)

(defmethod serve :get-data [{:keys [start per-page res]}]
  (put! res (subvec remote-data start (+ start per-page))))
