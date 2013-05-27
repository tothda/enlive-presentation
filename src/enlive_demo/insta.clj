;; emit*

;; snippet*

(ns enlive-demo.insta
  (:use [net.cgrand.enlive-html]
        [clojure.pprint :only [pprint]])
  (:require [compojure.core :as c]
            [ring.adapter.jetty :as jetty]))

(def p (comp println pprint))

;; html-content

(def h (html-resource "templates/heroes.html"))

;; SELECTORS
;; ---------

;; find the <h1> elemenets

;; how many <a> elements are

;; find <a> elements inside <li>'s

;; find the name of every hero

;; find the name of the first hero (->)

;; find the name of the hero in focus (attr? attr=)

;; how many <p> or <a> tags are insude <li>'s? (2 sets)

;; find all URL's (3)

;; Find all element with exactly 4 child elements. (pred)

;; Find all string nodes which longer than 20 chars. (text-pred)

;; Find all element with a direct text child which contains the text "Clojure". (has, re-pred)


;; TRANSFORMATIONS
;; ---------------


(sniptest "<p>foo</p>"
          [:p] (wrap :div))

(def n2 (html-snippet "<p>hello</p>"))

(at n2 [:p] (wrap :div))

(emit*
 (at n2 [:p] (wrap :div)))

(apply str
       (emit*
         (at n2 [:p] (wrap :div))))

;; Infrastructure

(declare t)

;; Compojure app
(c/defroutes app
  (c/GET "/" [] (t)))

(defonce server (jetty/run-jetty #'app {:port 8081 :join? false}))

(.stop server)
(.start server)

(defn t [] "Hello Meetup!")

(defmacro tt [ & forms]
  `(defn t []
     (emit* (at h ~@forms))))

(tt)

;; change the title and the h1 to "Clojure Newbies" (content)

;; wrap every link in <strong> tags. (wrap)

;; Change the background color of the hero in focus to yellow. (set-attr)

;; Add reference to the Enlive project in @cgrand's <li> (data structure, html)

;; Change the @cgrand's twitter link (do->)

;; Use clone-for for occupy Hungary (clone-for)
