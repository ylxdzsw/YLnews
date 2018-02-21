<template>
  <v-ons-page>
    <v-ons-toolbar>
      <div class="left">
        <v-ons-back-button>返回</v-ons-back-button>
      </div>
      <div class="center">{{ detail.title || news.title || "加载中..." }}</div>
    </v-ons-toolbar>

    <article>
      <h1 v-show="detail.title || news.title"> {{ detail.title || news.title }} </h1>
      <div v-show="info"> {{ info }} </div>
      <video v-for="x in detail.video" :key="x" :src="x" controls />
      <p v-for="x in detail.image" :key="x">
        <img :src="x" />
      </p>
      <p v-for="x in detail.text" :key="x"> {{ x }} </p>
    </article>
  </v-ons-page>
</template>

<script>
export default {
  props: ['news'],
  data() {
    return {
      detail: {}
    }
  },
  computed: {
    info() {
      const seg = []
      
      if (this.detail.info)
        seg.push(this.detail.info)

      if (this.detail.time)
        seg.push(this.detail.time)
      else if (this.detail.date)
        seg.push(this.detail.date)
      else
        seg.push(this.news.date)

      return seg.join(' ')
    }
  },
  created() {
    scraper.fetchNewsDetail(this.news.url).then(x=>this.detail = x)
  }
}
</script>

<style scoped>

</style>