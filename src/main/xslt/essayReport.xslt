<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:output method="html" indent="yes"/>
	<xsl:template match="/">
		<html>
			<body>
				<h2 style="background-color:#4838B0;color:#F2F4FF;border:2px solid #897FC9;">Summary Essays reports</h2>
				<p style="text-align:left;background-color:SlateGray; color:GhostWhite;font-size:16">Files</p>
				<table width="100%">
					<xsl:for-each select="report/file">
						<xsl:sort select="info/@pagesBySymbols" order="descending"/>
						<xsl:choose>
							<xsl:when test="(position() mod 2) != 1">
								<tr>
									<td style="border:2px solid #CACACA;background-color:Lavender">
										<a href="{@name}">
											<xsl:value-of select="@name"/>
										</a>
										<br/>Has <xsl:value-of select="info/@symbols"/> symbols <xsl:value-of select="info/@sentances"/> sentances and <xsl:value-of select="info/@allWords"/> words.
                 Pages based on symbols count: <xsl:value-of select="info/@pagesBySymbols"/> , pages based on words count <xsl:value-of select="info/@pagesByWords"/>
									</td>
								</tr>
							</xsl:when>
							<xsl:otherwise>
								<tr>
									<td style="border:2px solid #CACACA;background-color:GhostWhite">
										<a href="{@name}">
											<xsl:value-of select="@name"/>
										</a>
										<br/>Has <xsl:value-of select="info/@symbols"/> symbols <xsl:value-of select="info/@sentances"/> sentances and <xsl:value-of select="info/@allWords"/> words.
                 Pages based on symbols count: <xsl:value-of select="info/@pagesBySymbols"/> , pages based on words count <xsl:value-of select="info/@pagesByWords"/>
									</td>
								</tr>
							</xsl:otherwise>
						</xsl:choose>
						<xsl:variable name="name" select="@name"/>
					</xsl:for-each>
				</table>
			</body>
		</html>
	</xsl:template>
</xsl:stylesheet>
