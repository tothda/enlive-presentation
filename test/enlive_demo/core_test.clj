(ns enlive-demo.core-test
  (:use clojure.test
        enlive-demo.core)
  (:require [net.cgrand.enlive-html :as e]))

(def fixture-line1 "
<tr>
  <td align='right' valign='top' class='title'>1.</td>
  <td>
    <center>
      <a id='up_5732718' onclick='return vote(this)' href='vote?for=5732718&amp;dir=up&amp;by=tothda&amp;auth=c84a18f2215b4ca1d89700c137fc521027b8488a&amp;whence=%6e%65%77%73'>
      <img src='grayarrow.gif' border='0' vspace='3' hspace='2'>
    </a>
    <span id='down_5732718'></span>
    </center>
  </td>
  <td class='title'>
    <a href='http://firespotting.com/newest'>A Hacker News For Ideas</a>
    <span class='comhead'> (firespotting.com) </span>
  </td>
</tr>
")

(def fixture-line2 "
<tr>
  <td colspan='2'></td>
  <td class='subtext'>
    <span id='score_5732718'>56 points</span>
    by
    <a href='user?id=newernpguy'><font color='#3c963c'>newernpguy</font></a>
    1 hour ago  |
    <a href='item?id=5732718'>11 comments</a>
  </td>
</tr>")

(def l1 (e/html-snippet fixture-line1))
(def l2 (e/html-snippet fixture-line2))

(run-tests)

(deftest extractions
  (is (= (extract-title l1)
         {:title "A Hacker News For Ideas"
          :title-link "http://firespotting.com/newest"}))

  (is (= (extract-user l2)
         {:user "newernpguy"}))

  (is (= (extract-ago l2)
         {:ago "1 hour ago"}))

  (is (= (extract-points l2)
     {:points "56 points"}))

  (is (= (extract-comments l2)
       {:comments "11 comments"
        :comments-link "item?id=5732718"}))

  (is (= (extract-data [l1 l2])
         {:points "56 points"
          :user "newernpguy"
          :ago "1 hour ago"
          :title "A Hacker News For Ideas"
          :title-link "http://firespotting.com/newest"
          :comments "11 comments"
          :comments-link "item?id=5732718"}))
)

(-> "<div><h1>h</h1><p>1</p><p>2</p><p>3</p></div>"
    e/html-snippet
    (e/select {[[:p (e/nth-of-type 1)]] [[:p (e/nth-of-type 2)]]})
    ; (e/select [[:p (e/nth-of-type 1)]])
    ; (e/select [[:p (e/nth-of-type 1)]])
    ;e/texts
    )

(e/html-snippet )

(e/select l1 [:td])

