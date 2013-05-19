(ns enlive-demo.core
  (:use [clojure.pprint :only [pprint]])
  (:require [net.cgrand.enlive-html :as e]
            [compojure.core :as c]))

;; https://groups.google.com/forum/#!topic/light-table-discussion/W1FBd9Ydgfc
(def p (comp println pprint))
;; http://stackoverflow.com/a/2352280/1363047
(defmacro dbg[x] `(let [x# ~x] (println "dbg:" '~x "=" x#) x#))

;; HN scrape

(def base-url "https://news.ycombinator.com/")

(defn fetch-url [url]
  (e/html-resource (java.net.URL. url)))

(defn hn-dom [] (fetch-url base-url))

(defn content []
 (first (e/select (hn-dom) [:body :> :center :> :table [:tr (e/nth-child 3)]])))

;; extract fields

(defn extract-title [line1]
  (let [elem (first (e/select line1 [[:td (e/nth-child 3)] :a]))]
    {:title (e/text elem)
     :title-link (get-in elem [:attrs :href])}))

(defn extract-user [html]
  {:user (e/text (first (e/select html [:tr :td.subtext [:a (e/nth-of-type 1)]])))})

(defn extract-ago [html]
  {:ago (re-find
          #"\d+ \p{Alpha}+ ago"
          (e/text (first (e/select html [:tr :td.subtext]))))})

(defn extract-comments [html]
  (let [elem (-> html
                 (e/select [:td.subtext [:a (e/nth-of-type 2)]])
                  first)]
    {:comments (e/text elem)
     :comments-link (get-in elem [:attrs :href])}))

(defn extract-points [html]
  {:points (e/text (first (e/select html [[:td (e/nth-child 2)] :span])))})

(defn extract-data [[line1 line2]]
  (merge (extract-title line1)
         (extract-user line2)
         (extract-ago line2)
         (extract-points line2)
         (extract-comments line2)))

(defn extract-lines [container]
  (let [c (content)]
    (map vector
       (butlast (e/select c [:table [:tr (e/has [:td.title])]]))
       (e/select c [:table [:tr (e/has [:td.subtext])]]))))

;; Rendering

(e/defsnippet item-model "templates/hn.html" [:#content [:li (e/nth-child 1)]]
  [{:keys [title domain points title-link user comments comments-link]}]
  [e/root :> :a] (e/do->
                  (e/content title)
                  (e/set-attr :href title-link))
  [:.foot :span] (e/content points)
  [:.foot [:a (e/nth-of-type 1)]] (e/do->
                                   (e/content user)
                                   (e/set-attr :href (str base-url "user?id=" user)))
  [:.foot [:a (e/nth-of-type 2)]] (e/do->
                                   (e/content comments)
                                   (e/set-attr :href (str base-url comments-link))))

(e/deftemplate page "templates/hn.html" [items]
  [:#content :ol] (e/content (map item-model items)))

(defn render-page []
  (let [data (map extract-data (extract-lines (content)))]
    (page data)))

(c/defroutes app
  (c/GET "/" [] (render-page)))

