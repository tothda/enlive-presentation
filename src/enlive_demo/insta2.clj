;; emit*

;; snippet*

(ns enlive-demo.insta2
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
(select h [:h1])

;; how many <a> elements are
(count (select h [:a]))

;; find <a> elements inside <li>'s
(count (select h [:li :> :a]))

;; find the name of every hero
(map text (select h [:#list :.name]))

;; find the name of the first hero
(-> h
    (select [[:li (nth-child 1)] :h2])
    first
    text)

;; find the name of the hero in focus (2)

(-> h
    (select [[:li (attr= :data-focus "true")] :h2])
    first
    text)

(-> h (select [[:li (attr? :data-focus)] :h2]) first text)

;; how many <p> or <a> tags are insude <li>'s? (2)

(count (select h #{[:li :p] [:li :a]}))

(count (select h [:li #{[:p] [:a]}]))


;; find all URL's (3)
(map :href (map :attrs (select h [:a])))

(map #(get-in % [:attrs :href]) (select h [:a]))

(map (comp :href :attrs) (select h [:a]))

;; Find all element with exactly 4 child elements.
(select h [[(pred #(= 4 (count (remove string? (:content %)))))]])

(-> h (select [:div]) first :content)

;; Find all string nodes which longer than 20 chars.

(select h [[(text-pred #(< 20 (count %)))]])

;; Find all element with a direct text child which contains the text "Clojure".

(select h [[:* (has [:> (re-pred #"(?i).*Clojure.*")])]])


;; TRANSFORMATIONS
;; ---------------




;; ==========================================
;; Transformations

(sniptest "<p>foo</p>"
          [:p] (wrap :div))

(def n2 (html-snippet "<p>hello</p>"))
n2

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

;; change the title and the h1 to "Clojure Newbies"

(tt #{[:title] [:h1]} (content "Clojure Newbies"))

;; wrap every link in <strong> tags.

(tt [:a] (wrap :strong))

;; Change the background color of the hero in focus to yellow.

(tt [(attr? :data-focus)] (set-attr :style "background-color:yellow"))


;; Add reference to the Enlive project in @cgrand's <li>

(tt [(attr? :data-focus)] (append {:tag :a :attrs {:href "https://github.com/cgrand/enlive"}
                                   :content ["Enlive"]}))

(tt [(attr? :data-focus)] (append (html [:a
                                         {:href "https://github.com/cgrand/enlive"}
                                         "Enlive"])))

;; Change the @cgrand's twitter link

(tt [(attr? :data-focus) :a] (do->
                               (set-attr :href "https://twitter.com/tothda")
                               (content "Hijacked.")))

;; Use clone-for for creating Hungary
(tt
 [[:li (but (nth-child 1))]] nil
 [[:li (nth-child 1)]] (clone-for [vezer ["Almos" "Elod" "Ond" "Kond" "Tas" "Huba" "Tohotom"]]
                                     [:h2] (content vezer)
                                     [:p] (content "Vezír")
                                     [:a] nil))
